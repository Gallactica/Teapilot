package me.teawin.teapilot;

import com.google.gson.*;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import me.teawin.teapilot.mixin.accessor.HandledScreenAccessor;
import me.teawin.teapilot.protocol.type.SlotItem;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.entity.*;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.decoration.InteractionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static me.teawin.teapilot.protocol.GsonConfigurator.gson;

public class JsonUtils {
    public static JsonElement fromBlockState(BlockState blockState) {
        JsonObject blockObject = new JsonObject();
        blockState.getRegistryEntry()
                .getKey()
                .ifPresent(blockRegistryKey -> blockObject.addProperty("id", blockRegistryKey.getValue()
                        .toString()));
        blockObject.add("properties", serializeState(blockState));
        return blockObject;
    }

    public static JsonElement fromScreen(Screen screen) {
        JsonObject screenJson = new JsonObject();
        screenJson.add("type", new JsonPrimitive("undefined"));

        if (!(screen instanceof HandledScreen<?> containerScreen)) {
            return screenJson;
        }
        HandledScreenAccessor containerScreenAccessor = (HandledScreenAccessor) screen;

        screenJson.add("title", JsonUtils.fromText(screen.getTitle()));

        screenJson.add("width", new JsonPrimitive(screen.width));
        screenJson.add("height", new JsonPrimitive(screen.height));
        screenJson.add("x", new JsonPrimitive(containerScreenAccessor.getX()));
        screenJson.add("y", new JsonPrimitive(containerScreenAccessor.getY()));
        screenJson.add("titleX", new JsonPrimitive(containerScreenAccessor.getTitleX()));
        screenJson.add("titleY", new JsonPrimitive(containerScreenAccessor.getTitleY()));
        screenJson.add("backgroundWidth", new JsonPrimitive(containerScreenAccessor.getBackgroundWidth()));
        screenJson.add("backgroundHeight", new JsonPrimitive(containerScreenAccessor.getBackgroundHeight()));
        screenJson.add("playerInventoryTitleX", new JsonPrimitive(containerScreenAccessor.getPlayerInventoryTitleX()));
        screenJson.add("playerInventoryTitleY", new JsonPrimitive(containerScreenAccessor.getPlayerInventoryTitleY()));

        JsonArray slots = new JsonArray();
        for (Slot slot : containerScreen.getScreenHandler().slots) {
            JsonObject jsonSlot = new JsonObject();
            jsonSlot.addProperty("id", slot.id);
            jsonSlot.addProperty("x", slot.x);
            jsonSlot.addProperty("y", slot.y);
            slots.add(jsonSlot);
        }
        screenJson.add("slots", slots);

        if (containerScreen instanceof GenericContainerScreen genericContainerScreen) {
            screenJson.add("type", new JsonPrimitive("chest"));
            screenJson.add("rows", new JsonPrimitive(genericContainerScreen.getScreenHandler()
                    .getRows()));
        }

        if (containerScreen instanceof InventoryScreen) {
            screenJson.add("type", new JsonPrimitive("player_inventory"));
        }

        return screenJson;

    }

    public static JsonElement fromPosition(double x, double y, double z) {
        JsonObject pos = new JsonObject();
        pos.addProperty("x", x);
        pos.addProperty("y", y);
        pos.addProperty("z", z);
        return pos;
    }

    public static JsonElement fromPosition(int x, int y, int z) {
        JsonObject pos = new JsonObject();
        pos.addProperty("x", x);
        pos.addProperty("y", y);
        pos.addProperty("z", z);
        return pos;
    }

    public static JsonElement fromPosition(Position position) {
        return fromPosition(position.getX(), position.getY(), position.getZ());
    }

    public static JsonElement fromPosition(BlockPos position) {
        return fromPosition(position.getX(), position.getY(), position.getZ());
    }

    public static @Nullable JsonElement fromItemStack(ItemStack itemStack) {
        if (itemStack.getItem()
                .equals(Items.AIR)) {
            return null;
        }

        assert MinecraftClient.getInstance().world != null;
        DataResult<JsonElement> result = ItemStack.CODEC.encodeStart(
                MinecraftClient.getInstance().world.getRegistryManager()
                        .getOps(JsonOps.INSTANCE), itemStack);
        return result.getPartialOrThrow();
    }

    public static JsonElement fromText(Text text) {
        if (Teapilot.flags.isEnabled("EXPERIMENT_TEXT_SERIALIZATION")) {
            JsonElement json = TextCodecs.CODEC.encodeStart(JsonOps.INSTANCE, text)
                    .getOrThrow();
            JsonPrimitive content = new JsonPrimitive(text.getString()
                    .replaceAll("§.", ""));

            JsonObject jsonElement = new JsonObject();
            jsonElement.add("json", json);
            jsonElement.add("text", content);

            return jsonElement;
        }

        return TextCodecs.CODEC.encodeStart(JsonOps.INSTANCE, text)
                .getOrThrow();
    }

    public static JsonElement fromTextList(List<Text> textList) {
        JsonArray jsonArray = new JsonArray();

        for (Text text : textList) {
            jsonArray.add(fromText(text));
        }

        return jsonArray;
    }

    public static List<Text> toTextList(JsonArray jsonArray) {
        List<Text> textList = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            textList.add(toText(jsonElement));
        }
        return textList;
    }

    public static JsonElement fromServerInfo(ServerInfo info) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("address", info.address);
        jsonObject.addProperty("ping", info.ping);
        jsonObject.addProperty("online", !info.isLocal());

        return jsonObject;
    }

    public static JsonElement fromEntity(Entity entity) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("type", Registries.ENTITY_TYPE.getId(entity.getType())
                .toString());
        jsonObject.addProperty("id", entity.getId());
        jsonObject.addProperty("uuid", entity.getUuid()
                .toString());
        jsonObject.add("pos", fromPosition(entity.getPos()));
        jsonObject.add("vel", fromPosition(entity.getVelocity()));

        JsonObject lookObject = new JsonObject();

        lookObject.addProperty("yaw", entity.getYaw());
        lookObject.addProperty("head_yaw", entity.getHeadYaw());
        lookObject.addProperty("pitch", entity.getPitch());

        jsonObject.add("look", lookObject);

        JsonObject boundingBoxObject = new JsonObject();
        boundingBoxObject.addProperty("width", entity.getWidth());
        boundingBoxObject.addProperty("height", entity.getHeight());

        jsonObject.add("bounding_box", boundingBoxObject);

        if (entity.hasCustomName()) {
            jsonObject.add("customName", JsonUtils.fromText(entity.getCustomName()));
        }

        jsonObject.addProperty("glowing", entity.isGlowing());
        jsonObject.addProperty("invisible", entity.isInvisible());

        if (entity instanceof LivingEntity livingEntity) {
            JsonArray items = new JsonArray();
            AtomicBoolean hasItem = new AtomicBoolean(false);
            livingEntity.getArmorItems()
                    .forEach(itemStack -> {
                        items.add(JsonUtils.fromItemStack(itemStack));
                        if (!itemStack.getItem()
                                .equals(Items.AIR)) hasItem.set(true);
                    });
            if (hasItem.get()) jsonObject.add("armor", items);
            if (!livingEntity.getMainHandStack()
                    .getItem()
                    .equals(Items.AIR))
                jsonObject.add("main_hand", JsonUtils.fromItemStack(livingEntity.getMainHandStack()));
            if (!livingEntity.getOffHandStack()
                    .getItem()
                    .equals(Items.AIR))
                jsonObject.add("off_hand", JsonUtils.fromItemStack(livingEntity.getOffHandStack()));
        }

        if (entity instanceof InteractionEntity interactionEntity) {
            jsonObject.add("width", new JsonPrimitive(interactionEntity.getInteractionWidth()));
            jsonObject.add("height", new JsonPrimitive(interactionEntity.getInteractionHeight()));
        }

        if (entity instanceof ItemEntity itemEntity)
            jsonObject.add("item", JsonUtils.fromItemStack(itemEntity.getStack()));

        if (entity instanceof DisplayEntity.ItemDisplayEntity itemDisplayEntity) {
            jsonObject.add("item", JsonUtils.fromItemStack(itemDisplayEntity.getItemStack()));
            jsonObject.addProperty("mode", itemDisplayEntity.getTransformationMode()
                    .asString());
        }

        if (entity instanceof DisplayEntity.BlockDisplayEntity blockDisplayEntity) {
            jsonObject.add("block", JsonUtils.fromBlockState(blockDisplayEntity.getBlockState()));
        }

        if (entity instanceof DisplayEntity.TextDisplayEntity textDisplayEntity) {
            jsonObject.add("text", JsonUtils.fromText(textDisplayEntity.getText()));
            if (Teapilot.flags.isEnabled("EXTENDED_TEXT_DISPLAY_ENTITY")) {
                jsonObject.addProperty("billboard", textDisplayEntity.getBillboardMode()
                        .asString());
                jsonObject.addProperty("lineWidth", textDisplayEntity.getLineWidth());
                jsonObject.addProperty("background", textDisplayEntity.getBackground());
                jsonObject.addProperty("brightness", textDisplayEntity.getBrightness());
            }
        }

        if (entity instanceof DisplayEntity displayEntity) {
            AffineTransformation transformation = DisplayEntity.getTransformation(displayEntity.getDataTracker());
            jsonObject.add("scale", fromVector(transformation.getScale()));
            jsonObject.add("translation", fromVector(transformation.getTranslation()));
            jsonObject.add("left_rotation", fromQuaternion(transformation.getLeftRotation()));
            jsonObject.add("right_rotation", fromQuaternion(transformation.getRightRotation()));
        }

        if (entity instanceof AreaEffectCloudEntity areaEffectCloudEntity) {
            jsonObject.addProperty("effect", areaEffectCloudEntity.getParticleType()
                    .getType()
                    .toString());
            jsonObject.addProperty("radius", areaEffectCloudEntity.getRadius());
            jsonObject.addProperty("age", areaEffectCloudEntity.age);
        }

        if (entity instanceof Ownable ownable) {
            if (ownable.getOwner() != null) jsonObject.addProperty("owner", ownable.getOwner()
                    .getUuid()
                    .toString());
        }

        Entity vehicle = entity.getVehicle();
        if (vehicle != null) {
            jsonObject.addProperty("vehicle", vehicle.getUuid()
                    .toString());
            if (Teapilot.flags.isEnabled("EXTENDED_VEHICLE_ENTITY"))
                jsonObject.add("vehicleEntity", JsonUtils.fromEntity(vehicle));
        }

        if (entity.hasPassengers()) {
            List<Entity> passengerList = entity.getPassengerList();
            JsonArray jsonArray = new JsonArray();
            for (Entity passenger : passengerList) {
                jsonArray.add(passenger.getUuid()
                        .toString());
            }
            jsonObject.add("passengers", jsonArray);
        }

        if (Teapilot.flags.isEnabled("EXPERIMENTAL_ENTITY_NBT")) {
            NbtCompound nbtCompound = new NbtCompound();
            entity.writeNbt(nbtCompound);
            DataResult<JsonElement> jsonElementDataResult = NbtCompound.CODEC.encodeStart(JsonOps.INSTANCE,
                    nbtCompound);
            jsonObject.add("nbt", jsonElementDataResult.getOrThrow());
        }

        return jsonObject;
    }

    public static JsonElement fromQuaternion(Quaternionf quaternion) {
        JsonObject pos = new JsonObject();
        pos.addProperty("x", quaternion.x);
        pos.addProperty("y", quaternion.y);
        pos.addProperty("z", quaternion.z);
        pos.addProperty("w", quaternion.w);
        return pos;
    }

    public static JsonElement fromVector(Vector3f vector) {
        JsonObject pos = new JsonObject();
        pos.addProperty("x", vector.x);
        pos.addProperty("y", vector.y);
        pos.addProperty("z", vector.z);
        return pos;
    }

    private static JsonElement serializeState(BlockState properties) {
        DataResult<JsonElement> result = BlockState.CODEC.encodeStart(JsonOps.INSTANCE, properties);
        return result.getOrThrow()
                .getAsJsonObject()
                .get("Properties");
    }

    public static Text toText(JsonElement json) {
        return TextCodecs.CODEC.decode(JsonOps.INSTANCE, gson.fromJson(json, JsonElement.class))
                .getOrThrow()
                .getFirst();
    }

    public static BlockPos toBlockPos(JsonElement json) {
        JsonObject positionJson = json.getAsJsonObject();
        int x = positionJson.get("x")
                .getAsInt();
        int y = positionJson.get("y")
                .getAsInt();
        int z = positionJson.get("z")
                .getAsInt();
        return new BlockPos(x, y, z);
    }

    public static Position toPosition(JsonElement json) {
        JsonObject positionJson = json.getAsJsonObject();
        double x = positionJson.get("x")
                .getAsDouble();
        double y = positionJson.get("y")
                .getAsDouble();
        double z = positionJson.get("z")
                .getAsDouble();
        return new Vec3d(x, y, z);
    }

    public static JsonElement fromSlotItem(SlotItem slotItem) {
        JsonObject slotItemJson = new JsonObject();
        slotItemJson.add("slot", new JsonPrimitive(slotItem.getSlot()));
        slotItemJson.add("item", fromItemStack(slotItem.getItem()));
        return slotItemJson;
    }
}
