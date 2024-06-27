package me.teawin.teapilot;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.teawin.teapilot.mixin.AbstractSignEditScreenAccessor;
import me.teawin.teapilot.mixin.ParticleAccessor;
import me.teawin.teapilot.protocol.TeapilotServer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AbstractSignEditScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.management.ManagementFactory;

public class Teapilot implements ModInitializer {
    public static FlagsManager flagsManager = new FlagsManager();
    public static RequestDispatcher manager = new RequestDispatcher();
    public static TeapilotServer teapilotServer = getTcpServer();
    public static final Logger LOGGER = LoggerFactory.getLogger("Teapilot");

    public static boolean isDebug = ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("jdwp");

    @Override
    public void onInitialize() {

        LOGGER.info("Running with packet logging " + (isDebug ? "enabled" : "disabled"));

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
        flagsManager.disable("PACKET_PARTICLE");

        flagsManager.set("DEBUG_PACKET_LOGGER", isDebug);

        ClientSendMessageEvents.ALLOW_CHAT.register(message -> {
            if (flagsManager.isDisabled("INTERCEPT_SEND_CHAT_MESSAGE")) return true;

            JsonObject response = new JsonObject();
            response.addProperty("event", TeapilotEvents.SEND_MESSAGE.toString());
            response.addProperty("text", message);
            teapilotServer.broadcast(response);

            return false;
        });

        ClientSendMessageEvents.ALLOW_COMMAND.register(message -> {
            if (flagsManager.isDisabled("INTERCEPT_SEND_CHAT_COMMAND")) return true;

            JsonObject response = new JsonObject();
            response.addProperty("event", TeapilotEvents.SEND_COMMAND.toString());
            response.addProperty("command", message);
            teapilotServer.broadcast(response);

            return false;
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            JsonObject response = new JsonObject();
            response.addProperty("event", TeapilotEvents.CONNECT.toString());
            if (client.getNetworkHandler() != null && client.getNetworkHandler().getServerInfo() != null)
                response.add("server", JsonUtils.fromServerInfo(client.getNetworkHandler().getServerInfo()));
            else response.add("server", JsonNull.INSTANCE);
            teapilotServer.broadcast(response);
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            JsonObject response = new JsonObject();
            response.addProperty("event", TeapilotEvents.DISCONNECT.toString());
            teapilotServer.broadcast(response);
        });

        ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof FishingBobberEntity fishingBobberEntity) {
                if (fishingBobberEntity.getPlayerOwner() != null) {
                    if (fishingBobberEntity.getPlayerOwner().equals(MinecraftClient.getInstance().player)) {
                        var event = TeapilotEvents.createEvent(TeapilotEvents.FISH_HOOK);
                        event.add("hook", JsonUtils.fromEntity(entity));
                        teapilotServer.broadcast(event);
                    }
                }
            }

            if (flagsManager.isDisabled("PACKET_ENTITY")) return;

            JsonObject event = TeapilotEvents.createEvent(TeapilotEvents.ENTITY_SPAWN);
            event.add("entity", JsonUtils.fromEntity(entity));

            teapilotServer.broadcast(event);
        });

        ClientEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
            if (flagsManager.isDisabled("PACKET_ENTITY")) return;

            JsonObject event = TeapilotEvents.createEvent(TeapilotEvents.ENTITY_DESPAWN);
            event.add("entity", JsonUtils.fromEntity(entity));

            teapilotServer.broadcast(event);
        });

        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            JsonObject jsonObject = new JsonObject();

            assert MinecraftClient.getInstance().player != null;

            jsonObject.addProperty("event", overlay ? TeapilotEvents.OVERLAY.toString() : TeapilotEvents.CHAT.toString());
            jsonObject.addProperty("text", message.getString());
            jsonObject.add("message", Text.Serializer.toJsonTree(message));

            if (flagsManager.get("PACKET_CHAT") && !overlay) {
                teapilotServer.broadcast(jsonObject);
            } else if (flagsManager.get("PACKET_OVERLAY") && overlay) {
                teapilotServer.broadcast(jsonObject);
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

            jsonObject.addProperty("event", TeapilotEvents.POSITION.toString());
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

            teapilotServer.broadcast(jsonObject);
        });

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> client.getSoundManager().registerListener((sound, soundSet) -> {
            if (flagsManager.isDisabled("PACKET_SOUND")) return;

            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("event", TeapilotEvents.SOUND.toString());
            jsonObject.addProperty("id", sound.getId().toString());
            jsonObject.addProperty("volume", sound.getVolume());
            jsonObject.addProperty("pitch", sound.getPitch());
            jsonObject.add("position", JsonUtils.fromPosition(sound.getX(), sound.getY(), sound.getZ()));

            teapilotServer.broadcast(jsonObject);
        }));

        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof AbstractSignEditScreen signEditScreen) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("event", TeapilotEvents.SIGN_OPEN.toString());

                JsonArray rows = new JsonArray();
                String[] messages = ((AbstractSignEditScreenAccessor) signEditScreen).getMessages();
                for (String message : messages) {
                    rows.add(new JsonPrimitive(message));
                }
                jsonObject.add("rows", rows);

                teapilotServer.broadcast(jsonObject);
            }
        });

        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (flagsManager.isDisabled("PACKET_CONTAINER")) return;

            if (!(screen instanceof HandledScreen<?> containerScreen)) {
                return;
            }
            var event = TeapilotEvents.createEvent(TeapilotEvents.CONTAINER_OPEN);
            event.addProperty("name", screen.getClass().getSimpleName());
            event.add("screen", JsonUtils.fromScreen(containerScreen));
            Teapilot.teapilotServer.broadcast(event);
        });
    }

    public static void broadcastEntityUpdate(Entity entity) {
        if (flagsManager.isDisabled("PACKET_ENTITY")) return;
        JsonObject event = TeapilotEvents.createEvent(TeapilotEvents.ENTITY_UPDATE);
        event.add("entity", JsonUtils.fromEntity(entity));
        teapilotServer.broadcast(event);
    }

    public static void broadcastParticle(Particle particle) {
        if (flagsManager.isDisabled("PACKET_PARTICLE")) return;

        ParticleAccessor accessor = (ParticleAccessor) particle;

        JsonObject response = new JsonObject();

        response.addProperty("event", TeapilotEvents.PARTICLE.toString());
        response.add("position", JsonUtils.fromPosition(accessor.getX(), accessor.getY(), accessor.getZ()));
        response.addProperty("name", particle.getClass().getSimpleName());

        teapilotServer.broadcast(response);
    }

    @NotNull
    private static TeapilotServer getTcpServer() {

        TeapilotServer server;
        try {
            server = new TeapilotServer(manager);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        server.start();

        return server;
    }
}
