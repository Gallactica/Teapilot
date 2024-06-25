package me.teawin.soulkeeper;

import com.mojang.datafixers.util.Pair;
import me.teawin.soulkeeper.mixin.HandledScreenAccessor;
import me.teawin.soulkeeper.mixin.MinecraftClientAccessor;
import me.teawin.soulkeeper.mixin.ParticleAccessor;
import me.teawin.soulkeeper.proposal.RayCast;
import me.teawin.soulkeeper.proposal.ScoreboardStore;
import me.teawin.soulkeeper.proposal.TooltipExchanger;
import me.teawin.soulkeeper.protocol.TCPServer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.util.Window;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.google.gson.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Soulkeeper implements ModInitializer {
    public static FlagsManager flagsManager = new FlagsManager();
    public static TCPServer tcpServer = getTcpServer();
    public static final Logger LOGGER = LoggerFactory.getLogger("Soulkeeper");

    @Override
    public void onInitialize() {

        flagsManager.disable("INTERCEPT_CHAT");
        flagsManager.disable("INTERCEPT_SEND_CHAT_MESSAGE");
        flagsManager.disable("INTERCEPT_SEND_CHAT_COMMAND");
        flagsManager.disable("INTERCEPT_ITEM_TOOLTIP");

        flagsManager.disable("PACKET_OVERLAY");
        flagsManager.disable("PACKET_BLOCK_UPDATE");
        flagsManager.disable("PACKET_CHAT");
        flagsManager.disable("PACKET_GPS");
        flagsManager.disable("PACKET_SOUND");
        flagsManager.disable("PACKET_ENTITY");
        flagsManager.disable("PACKET_CONTAINER");

        flagsManager.enable("DEBUG_PACKET_LOGGER");

        ClientSendMessageEvents.ALLOW_CHAT.register(message -> {
            if (flagsManager.isDisabled("INTERCEPT_SEND_CHAT_MESSAGE")) return true;

            JsonObject response = new JsonObject();
            response.addProperty("event", SoulkeeperEvents.SEND_MESSAGE.toString());
            response.addProperty("text", message);
            tcpServer.broadcast(response);

            return false;
        });

        ClientSendMessageEvents.ALLOW_COMMAND.register(message -> {
            if (flagsManager.isDisabled("INTERCEPT_SEND_CHAT_COMMAND")) return true;

            JsonObject response = new JsonObject();
            response.addProperty("event", SoulkeeperEvents.SEND_COMMAND.toString());
            response.addProperty("command", message);
            tcpServer.broadcast(response);

            return false;
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            JsonObject response = new JsonObject();
            response.addProperty("event", SoulkeeperEvents.CONNECT.toString());
            if (client.getNetworkHandler() != null && client.getNetworkHandler().getServerInfo() != null)
                response.add("server", JsonUtils.fromServerInfo(client.getNetworkHandler().getServerInfo()));
            else response.add("server", JsonNull.INSTANCE);
            tcpServer.broadcast(response);
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            JsonObject response = new JsonObject();
            response.addProperty("event", SoulkeeperEvents.DISCONNECT.toString());
            tcpServer.broadcast(response);
        });

        ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof FishingBobberEntity fishingBobberEntity) {
                if (fishingBobberEntity.getPlayerOwner() != null) {
                    if (fishingBobberEntity.getPlayerOwner().equals(MinecraftClient.getInstance().player)) {
                        var event = SoulkeeperEvents.createEvent(SoulkeeperEvents.FISH_HOOK);
                        event.add("hook", JsonUtils.fromEntity(entity));
                        tcpServer.broadcast(event);
                    }
                }
            }

            if (flagsManager.isDisabled("PACKET_ENTITY")) return;

            JsonObject event = SoulkeeperEvents.createEvent(SoulkeeperEvents.ENTITY_SPAWN);
            event.add("entity", JsonUtils.fromEntity(entity));

            tcpServer.broadcast(event);
        });

        ClientEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
            if (flagsManager.isDisabled("PACKET_ENTITY")) return;

            JsonObject event = SoulkeeperEvents.createEvent(SoulkeeperEvents.ENTITY_DESPAWN);
            event.add("entity", JsonUtils.fromEntity(entity));

            tcpServer.broadcast(event);
        });

        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            JsonObject jsonObject = new JsonObject();

            assert MinecraftClient.getInstance().player != null;

            jsonObject.addProperty("event", overlay ? SoulkeeperEvents.OVERLAY.toString() : SoulkeeperEvents.CHAT.toString());
            jsonObject.addProperty("text", message.getString());
            jsonObject.add("message", Text.Serializer.toJsonTree(message));

            if (flagsManager.get("PACKET_CHAT") && !overlay) {
                tcpServer.broadcast(jsonObject);
            } else if (flagsManager.get("PACKET_OVERLAY") && overlay) {
                tcpServer.broadcast(jsonObject);
            }

            if (overlay) {
                return true;
            }

            return flagsManager.isDisabled("INTERCEPT_CHAT");
        });

        ClientTickEvents.END_WORLD_TICK.register(client -> {
            if (flagsManager.isDisabled("PACKET_GPS")) return;

            ClientPlayerEntity player = MinecraftClient.getInstance().player;

            if (player == null) return;

            JsonObject jsonObject = new JsonObject();

            JsonObject velocityObject = new JsonObject();

            velocityObject.addProperty("x", player.getVelocity().x);
            velocityObject.addProperty("y", player.getVelocity().y);
            velocityObject.addProperty("z", player.getVelocity().z);
            velocityObject.addProperty("sideways", player.sidewaysSpeed);
            velocityObject.addProperty("forward", player.forwardSpeed);
            velocityObject.addProperty("upward", player.upwardSpeed);
            velocityObject.addProperty("horizontal", player.horizontalSpeed);

            jsonObject.addProperty("event", SoulkeeperEvents.POSITION.toString());
            jsonObject.addProperty("eye", player.getEyeY());
            jsonObject.add("position", JsonUtils.fromPosition(player.getX(), player.getY(), player.getZ()));

            jsonObject.add("velocity", velocityObject);

            jsonObject.addProperty("yaw", (player.getHeadYaw() + 180) % 360 - 180);
            jsonObject.addProperty("pitch", player.getPitch());
            jsonObject.addProperty("raw_yaw", player.getHeadYaw());

            jsonObject.addProperty("is_flying", player.getAbilities().flying);
            jsonObject.addProperty("is_invulnerable", player.getAbilities().invulnerable);
            jsonObject.addProperty("is_sneak", player.isSneaking());
            jsonObject.addProperty("on_ground", player.isOnGround());

            JsonObject constantObject = new JsonObject();
            constantObject.addProperty("fly_speed", player.getAbilities().getFlySpeed());
            constantObject.addProperty("walk_speed", player.getAbilities().getWalkSpeed());

            jsonObject.add("consts", constantObject);

            tcpServer.broadcast(jsonObject);
        });

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            client.getSoundManager().registerListener((sound, soundSet) -> {
                if (flagsManager.isDisabled("PACKET_SOUND")) return;

                JsonObject jsonObject = new JsonObject();

                jsonObject.addProperty("event", SoulkeeperEvents.SOUND.toString());
                jsonObject.addProperty("id", sound.getId().toString());
                jsonObject.addProperty("volume", sound.getVolume());
                jsonObject.addProperty("pitch", sound.getPitch());
                jsonObject.add("position", JsonUtils.fromPosition(sound.getX(), sound.getY(), sound.getZ()));

                tcpServer.broadcast(jsonObject);
            });
        });

        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (flagsManager.isDisabled("PACKET_CONTAINER")) return;

            if (!(screen instanceof HandledScreen)) {
                return;
            }
            HandledScreenAccessor containerScreenAccessor = (HandledScreenAccessor) screen;
            HandledScreen containerScreen = (HandledScreen) screen;

            var event = SoulkeeperEvents.createEvent(SoulkeeperEvents.CONTAINER_OPEN);

            event.add("screen", JsonUtils.fromScreen(containerScreen));

            Soulkeeper.tcpServer.broadcast(event);
        });
    }

    public static void broadcastEntityUpdate(Entity entity) {
        if (flagsManager.isDisabled("PACKET_ENTITY")) return;
        JsonObject event = SoulkeeperEvents.createEvent(SoulkeeperEvents.ENTITY_UPDATE);
        event.add("entity", JsonUtils.fromEntity(entity));
        tcpServer.broadcast(event);
    }

    public static void broadcastParticle(Particle particle) {
        if (flagsManager.isDisabled("PACKET_PARTICLE")) return;

        ParticleAccessor accessor = (ParticleAccessor) particle;

        JsonObject response = new JsonObject();

        response.addProperty("event", SoulkeeperEvents.PARTICLE.toString());
        response.add("position", JsonUtils.fromPosition(accessor.getX(), accessor.getY(), accessor.getZ()));
//        response.addProperty("x", accessor.getX());
//        response.addProperty("y", accessor.getY());
//        response.addProperty("z", accessor.getZ());
        response.addProperty("name", particle.getClass().getSimpleName());

        tcpServer.broadcast(response);
    }


    private static CompletableFuture<JsonObject> getWindowStat() {
        JsonObject response = new JsonObject();

        CompletableFuture<JsonObject> future = new CompletableFuture<>();

        MinecraftClient.getInstance().execute(() -> {
            Window window = MinecraftClient.getInstance().getWindow();

            response.addProperty("width", window.getWidth());
            response.addProperty("height", window.getHeight());
            response.addProperty("x", window.getX());
            response.addProperty("y", window.getY());
            response.addProperty("scale", window.getScaleFactor());
            response.addProperty("fov", MinecraftClient.getInstance().options.getFov().getValue());
            response.addProperty("is_fullscreen", window.isFullscreen());

            future.complete(response);
        });

        return future;
    }

    @NotNull
    private static synchronized TCPServer getTcpServer() {
        RemoteProcedureManager manager = new RemoteProcedureManager();

        manager.register("scoreboard.sidebar", payload -> {
            assert MinecraftClient.getInstance().player != null;

            JsonObject response = new JsonObject();

            response.add("title", JsonUtils.fromText(ScoreboardStore.sidebarTitle));

            JsonArray jsonArray = new JsonArray();
            for (Pair<ScoreboardPlayerScore, Text> scoreboardPlayerScoreTextPair : ScoreboardStore.sidebarTexts) {
                jsonArray.add(JsonUtils.fromText(scoreboardPlayerScoreTextPair.getSecond()));
            }
            response.add("texts", jsonArray);


            return response;
        });

        manager.register("client", (payload) -> {
            CompletableFuture<JsonObject> stat = getWindowStat();
            try {
                return stat.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        manager.register("tooltip", payload -> {
            TooltipExchanger.textList = JsonUtils.toTextList(payload.get("texts").getAsJsonArray());
            return null;
        });

        manager.register("soulkeeper", payload -> {
            JsonObject response = new JsonObject();

            JsonArray methodsArray = new JsonArray();
            for (String str : manager.getMethods()) {
                methodsArray.add(new JsonPrimitive(str));
            }
            response.add("methods", methodsArray);

            JsonArray jsonArray = new JsonArray();
            for (String eventName : SoulkeeperEvents.asList()) {
                jsonArray.add(new JsonPrimitive(eventName));
            }
            response.add("events", jsonArray);

            JsonArray flagsArray = new JsonArray();
            for (Map.Entry<String, Boolean> entry : flagsManager.flags.entrySet()) {
                flagsArray.add(new JsonPrimitive(entry.getKey()));
            }
            response.add("flags", flagsArray);

            return response;
        });

        manager.register("player.raycast", payload -> {

            JsonObject response = new JsonObject();
            RayCast.ray(response);

            return response;

        });

        manager.register("chat", jsonObject -> {
            JsonElement text = jsonObject.get("text");
            JsonElement element = jsonObject.get("message");
            if (MinecraftClient.getInstance().player == null) return null;
            MinecraftClient.getInstance().player.sendMessage(element != null ? Text.Serializer.fromJson(element) : Text.of(text.getAsString()));
            return null;
        });

        manager.register("flags.set", jsonObject -> {
            Boolean state = jsonObject.get("state").getAsBoolean();
            String name = jsonObject.get("name").getAsString();
            flagsManager.set(name, state);

            JsonObject replyObject = new JsonObject();
            for (Map.Entry<String, Boolean> entry : flagsManager.flags.entrySet()) {
                replyObject.addProperty(entry.getKey(), entry.getValue());
            }
            return replyObject;
        });

        manager.register("flags", jsonObject -> {
            JsonObject replyObject = new JsonObject();
            for (Map.Entry<String, Boolean> entry : flagsManager.flags.entrySet()) {
                replyObject.addProperty(entry.getKey(), entry.getValue());
            }
            return replyObject;
        });

        Random random = new Random();

        manager.register("player.interact.use", payload -> {
            var player = MinecraftClient.getInstance().player;

            assert player != null;

            ((MinecraftClientAccessor) MinecraftClient.getInstance()).callDoItemUse();

            return null;
        });

        manager.register("player.interact.attack", payload -> {
            var player = MinecraftClient.getInstance().player;

            assert player != null;

            ((MinecraftClientAccessor) MinecraftClient.getInstance()).callDoAttack();

            return null;
        });

        manager.register("player.move.jump", payload -> {
            var player = MinecraftClient.getInstance().player;
            assert player != null;
            player.jump();

            return null;
        });

        manager.register("player.info", payload -> {

            var player = MinecraftClient.getInstance().player;

            assert player != null;

            var hungerManager = player.getHungerManager();

            JsonObject response = new JsonObject();

            response.addProperty("id", player.getId());
            response.addProperty("uuid", player.getUuid().toString());
            response.addProperty("hotbar_slot", player.getInventory().selectedSlot);

            response.addProperty("health", player.getHealth());
            response.addProperty("food", hungerManager.getFoodLevel());
            response.addProperty("saturation", hungerManager.getSaturationLevel());

            response.addProperty("exp", player.experienceLevel);
            response.addProperty("total_exp", player.totalExperience);

            if (player.fishHook != null) {
                response.add("fish_hook", JsonUtils.fromEntity(player.fishHook));
            }

            return response;
        });

        manager.register("player.inventory.hand", payload -> {
            JsonObject response = new JsonObject();

            assert MinecraftClient.getInstance().player != null;

            response.add("main", JsonUtils.fromItemStack(MinecraftClient.getInstance().player.getMainHandStack()));
            response.add("off", JsonUtils.fromItemStack(MinecraftClient.getInstance().player.getOffHandStack()));
            response.addProperty("slot", MinecraftClient.getInstance().player.getInventory().selectedSlot);

            return response;
        });

        manager.register("player.inventory.at", payload -> {
            assert MinecraftClient.getInstance().player != null;

            int slot = payload.get("slot").getAsInt();

            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            ItemStack itemStack = player.getInventory().getStack(slot);

            JsonObject response = new JsonObject();

            response.addProperty("slot", slot);
            response.add("item", JsonUtils.fromItemStack(itemStack));

            return response;
        });

        manager.register("player.inventory", payload -> {
            assert MinecraftClient.getInstance().player != null;

            JsonObject response = new JsonObject();

            ClientPlayerEntity player = MinecraftClient.getInstance().player;

            JsonArray jsonArray = new JsonArray();
            for (int i = 0; i < 27; i++) {
                int slotId = i + 9;
                ItemStack slotItem = player.getInventory().getStack(slotId);
                JsonObject s = new JsonObject();
                s.addProperty("slot", slotId);
                s.add("item", JsonUtils.fromItemStack(slotItem));
                jsonArray.add(s);
            }
            response.add("main", jsonArray);

            JsonArray jsonHotArray = new JsonArray();
            for (int slotId = 0; slotId < 9; slotId++) {
                ItemStack slotItem = player.getInventory().getStack(slotId);
                JsonObject s = new JsonObject();
                s.addProperty("slot", slotId);
                s.add("item", JsonUtils.fromItemStack(slotItem));
                jsonHotArray.add(s);
            }
            response.add("hotbar", jsonHotArray);

            JsonArray jsonArmorArray = new JsonArray();
            for (int i = 0; i < 4; i++) {
                int slotId = i + 36;
                ItemStack slotItem = player.getInventory().getStack(slotId);
                JsonObject s = new JsonObject();
                s.addProperty("slot", slotId);
                s.add("item", JsonUtils.fromItemStack(slotItem));
                jsonArmorArray.add(s);
            }
            response.add("armor", jsonArmorArray);

            JsonObject offHand = new JsonObject();
            offHand.addProperty("slot", 40);
            offHand.add("item", JsonUtils.fromItemStack(player.getInventory().getStack(40)));
            response.add("off_hand", offHand);

            JsonObject mainHand = new JsonObject();
            mainHand.addProperty("slot", player.getInventory().selectedSlot);
            mainHand.add("item", JsonUtils.fromItemStack(player.getInventory().getStack(player.getInventory().selectedSlot)));
            response.add("main_hand", mainHand);

            return response;
        });

        manager.register("player.inventory.open", payload -> {
            assert MinecraftClient.getInstance().player != null;
            assert MinecraftClient.getInstance().interactionManager != null;

            MinecraftClient.getInstance().execute(() -> {
                if (MinecraftClient.getInstance().interactionManager.hasRidingInventory()) {
                    MinecraftClient.getInstance().player.openRidingInventory();
                } else {
                    if (MinecraftClient.getInstance().currentScreen instanceof InventoryScreen) return;
                    MinecraftClient.getInstance().getTutorialManager().onInventoryOpened();
                    MinecraftClient.getInstance().setScreen(new InventoryScreen(MinecraftClient.getInstance().player));
                }
            });

            return null;
        });

        manager.register("player.inventory.close", payload -> {
            assert MinecraftClient.getInstance().player != null;

            if (MinecraftClient.getInstance().currentScreen != null) {
                MinecraftClient.getInstance().currentScreen.close();
            }

            return null;
        });

        manager.register("player.inventory.hand.select", payload -> {
            int slot = payload.get("slot").getAsInt();

            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            assert player != null;

            player.getInventory().selectedSlot = MathHelper.clamp(slot, 0, 8);
            return null;
        });

//        manager.register("player.move", payload -> {
//
//            float ix = payload.get("x").getAsFloat();
//            float iz = payload.get("z").getAsFloat();
//
//            ClientPlayerEntity player = MinecraftClient.getInstance().player;
//            assert player != null;
//
//            float s = player.getMovementSpeed();
//            float x = MathHelper.clamp(ix, -1, 1);
//            float z = MathHelper.clamp(iz, -1, 1);
//
//            player.travel(new Vec3d(x * s, 20, z * s));
//            return null;
//        });

//        manager.register("player.inventory.drop.slot", payload -> {
//            int slot = payload.get("slot").getAsInt();
//            boolean stack = payload.has("stack") && payload.get("stack").getAsBoolean();
//
//            if (MinecraftClient.getInstance().currentScreen instanceof InventoryScreen inventoryScreen) {
//                assert MinecraftClient.getInstance().player != null;
//                OptionalInt s = inventoryScreen.getScreenHandler().getSlotIndex(MinecraftClient.getInstance().player.getInventory(), slot);
//
//                if (stack) {
//                    inventoryScreen.getScreenHandler().onSlotClick(s.getAsInt(), 0, SlotActionType.PICKUP_ALL, MinecraftClient.getInstance().player);
//                    inventoryScreen.getScreenHandler().onSlotClick(-999, 0, SlotActionType.THROW, MinecraftClient.getInstance().player);
//                } else {
//                    inventoryScreen.getScreenHandler().onSlotClick(s.getAsInt(), 0, SlotActionType.THROW, MinecraftClient.getInstance().player);
//                }
//
//            }
//            return null;
//        });

        manager.register("player.inventory.hand.drop", payload -> {

            boolean stack = payload.has("stack") && payload.get("stack").getAsBoolean();

            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            assert player != null;

            player.dropSelectedItem(stack);

            return null;
        });

        manager.register("player.rotate", payload -> {
            double x = payload.get("x").getAsDouble();
            double y = payload.get("y").getAsDouble();

            PlayerEntity player = MinecraftClient.getInstance().player;
            assert player != null;

            player.changeLookDirection(x, y);

            JsonObject response = new JsonObject();

            response.addProperty("yaw", player.getYaw());
            response.addProperty("pitch", player.getPitch());

            return response;
        });

        manager.register("player.look.at", payload -> {
            double x = payload.get("x").getAsDouble();
            double y = payload.get("y").getAsDouble();
            double z = payload.get("z").getAsDouble();

            float around = payload.has("around") ? payload.get("around").getAsFloat() : 0f;

            boolean center;
            if (payload.has("center")) center = payload.get("center").getAsBoolean();
            else center = true;

            float around_pitch = 0;
            float around_yaw = 0;

            if (around != 0) {
                around_pitch = (random.nextFloat() * 2 - 1) * around / 2;
                around_yaw = (random.nextFloat() * 2 - 1) * around;
            }

            PlayerEntity player = MinecraftClient.getInstance().player;
            assert player != null;

            if (center) {
                x += .5;
                y += .5;
                z += .5;
            }

            Vec3d target = new Vec3d(x, y, z);

            Vec3d vec3d = EntityAnchorArgumentType.EntityAnchor.EYES.positionAt(player);
            double d = target.x - vec3d.x;
            double e = target.y - vec3d.y;
            double f = target.z - vec3d.z;
            double g = Math.sqrt(d * d + f * f);

            float pitch = MathHelper.wrapDegrees((float) (-(MathHelper.atan2(e, g) * 57.2957763671875)));
            float yaw = MathHelper.wrapDegrees((float) (MathHelper.atan2(f, d) * 57.2957763671875) - 90.0F);

            player.setPitch(pitch + around_pitch);
            player.setYaw(yaw + around_yaw);
            player.setHeadYaw(player.getYaw());
            player.setPitch(MathHelper.clamp(player.getPitch(), -90.0F, 90.0F));

            JsonObject response = new JsonObject();

            response.addProperty("around_yaw", around_yaw);
            response.addProperty("around_pitch", around_pitch);
            response.addProperty("yaw", player.getYaw());
            response.addProperty("pitch", player.getPitch());

            return response;
        });

        manager.register("player.container.info", jsonObject -> JsonUtils.fromScreen(MinecraftClient.getInstance().currentScreen).getAsJsonObject());

        manager.register("player.container", payload -> {
            JsonObject response = new JsonObject();

            response.add("type", new JsonPrimitive("empty"));

            Screen screen = MinecraftClient.getInstance().currentScreen;
            if (!(screen instanceof HandledScreen)) {
                return response;
            }

            HandledScreenAccessor containerScreenAccessor = (HandledScreenAccessor) screen;
            HandledScreen containerScreen = (HandledScreen) screen;

            response.add("title", JsonUtils.fromText(containerScreen.getTitle()));

            if (screen instanceof GenericContainerScreen genericContainerScreen) {
                int totalChestSlots = genericContainerScreen.getScreenHandler().getRows() * 9;

                JsonArray chestArray = new JsonArray();
                for (int i = 0; i < totalChestSlots; i++) {
                    ItemStack slotItem = genericContainerScreen.getScreenHandler().getSlot(i).getStack();
                    JsonObject s = new JsonObject();
                    s.addProperty("slot", i);
                    s.add("item", JsonUtils.fromItemStack(slotItem));
                    chestArray.add(s);
                }
                response.add("chest", chestArray);

                JsonArray inventoryArray = new JsonArray();
                for (int i = 0; i < 27; i++) {
                    int slotId = i + totalChestSlots;
                    ItemStack slotItem = genericContainerScreen.getScreenHandler().getSlot(slotId).getStack();
                    JsonObject s = new JsonObject();
                    s.addProperty("slot", slotId);
                    s.add("item", JsonUtils.fromItemStack(slotItem));
                    inventoryArray.add(s);
                }
                response.add("inventory", inventoryArray);

                JsonArray hotbarArray = new JsonArray();
                for (int i = 0; i < 9; i++) {
                    int slotId = i + totalChestSlots + 27;
                    ItemStack slotItem = genericContainerScreen.getScreenHandler().getSlot(slotId).getStack();
                    JsonObject s = new JsonObject();
                    s.addProperty("slot", slotId);
                    s.add("item", JsonUtils.fromItemStack(slotItem));
                    hotbarArray.add(s);
                }
                response.add("hotbar", hotbarArray);
            }

            return response;
        });

        manager.register("player.look", payload -> {
            float yaw = payload.get("yaw").getAsFloat();
            float pitch = payload.get("pitch").getAsFloat();
            float around = payload.has("around") ? payload.get("around").getAsFloat() : 0f;

            float around_pitch = 0;
            float around_yaw = 0;

            if (around != 0) {
                around_pitch = (random.nextFloat() * 2 - 1) * around / 2;
                around_yaw = (random.nextFloat() * 2 - 1) * around;
            }

            PlayerEntity player = MinecraftClient.getInstance().player;

            assert player != null;

            player.setPitch(pitch + around_pitch);
            player.setYaw(yaw + around_yaw);
            player.setPitch(MathHelper.clamp(player.getPitch(), -90.0F, 90.0F));

            JsonObject response = new JsonObject();

            response.addProperty("around_yaw", around_yaw);
            response.addProperty("around_pitch", around_pitch);
            response.addProperty("yaw", player.getYaw());
            response.addProperty("pitch", player.getPitch());

            return response;
        });

        manager.register("world.entity.get", payload -> {
            Entity entity = null;

            assert MinecraftClient.getInstance().world != null;

            if (payload.has("id")) {
                int id = payload.get("id").getAsInt();
                entity = MinecraftClient.getInstance().world.getEntityById(id);
            } else if (payload.has("uuid")) {
                UUID uuid = UUID.fromString(payload.get("uuid").getAsString());
                for (Entity worldEntity : MinecraftClient.getInstance().world.getEntities()) {
                    if (worldEntity.getUuid().equals(uuid)) {
                        entity = worldEntity;
                        break;
                    }
                }
            }

            JsonObject response = new JsonObject();

            if (entity == null) {
                response.add("entity", JsonNull.INSTANCE);
            } else {
                response.add("entity", JsonUtils.fromEntity(entity));
            }

            return response;
        });

        manager.register("world.block.get", payload -> {
            assert MinecraftClient.getInstance().player != null;

            int x = payload.get("x").getAsInt();
            int y = payload.get("y").getAsInt();
            int z = payload.get("z").getAsInt();

            JsonObject response = new JsonObject();

            BlockState blockState = MinecraftClient.getInstance().player.getWorld().getBlockState(new BlockPos(x, y, z));

            response.add("pos", JsonUtils.fromPosition(x, y, z));
            response.add("block", JsonUtils.fromBlockState(blockState));

            return response;
        });

        manager.register("player.send.message", payload -> {
            String text = payload.get("text").getAsString();
            var prev = flagsManager.get("INTERCEPT_SEND_CHAT_MESSAGE");
            flagsManager.set("INTERCEPT_SEND_CHAT_MESSAGE", false);
            Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatMessage(text);
            flagsManager.set("INTERCEPT_SEND_CHAT_MESSAGE", prev);
            return null;
        });

        manager.register("player.send.command", payload -> {
            String command = payload.get("command").getAsString();
            if (command.startsWith("/")) command = command.substring(1);
            var prev = flagsManager.get("INTERCEPT_SEND_CHAT_COMMAND");
            flagsManager.set("INTERCEPT_SEND_CHAT_COMMAND", false);
            Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatCommand(command);
            flagsManager.set("INTERCEPT_SEND_CHAT_COMMAND", prev);
            return null;
        });

        manager.register("player.send", payload -> {
            String value = payload.has("command") ? payload.get("command").getAsString() : payload.has("text") ? payload.get("text").getAsString() : payload.has("value") ? payload.get("value").getAsString() : null;
            assert value != null;
            if (value.startsWith("/")) {
                var prev = flagsManager.get("INTERCEPT_SEND_CHAT_COMMAND");
                flagsManager.set("INTERCEPT_SEND_CHAT_COMMAND", false);
                Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatCommand(value.substring(1));
                flagsManager.set("INTERCEPT_SEND_CHAT_COMMAND", prev);
            } else {
                var prev = flagsManager.get("INTERCEPT_SEND_CHAT_MESSAGE");
                flagsManager.set("INTERCEPT_SEND_CHAT_MESSAGE", false);
                Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatMessage(value);
                flagsManager.set("INTERCEPT_SEND_CHAT_MESSAGE", prev);
            }
            return null;
        });

        manager.register("protocol.error", payload -> {
            int x = payload.get("dev").getAsInt();
            var resp = new JsonObject();
            resp.addProperty("xxx", x);
            return resp;
        });

        manager.register("protocol.echo", payload -> payload);

        TCPServer server;
        try {
            server = new TCPServer(manager);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        server.start();

        return server;
    }
}
