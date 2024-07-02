package me.teawin.teapilot;

import com.google.gson.*;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import me.teawin.teapilot.mixin.accessor.HandledScreenAccessor;
import me.teawin.teapilot.protocol.type.SlotItem;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.PositionImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class JsonUtils {
    public static JsonElement fromBlockState(BlockState blockState) {
        JsonObject blockObject = new JsonObject();
        blockState.getRegistryEntry().getKey()
                .ifPresent(blockRegistryKey -> blockObject.addProperty("id", blockRegistryKey.getValue().toString()));
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
            screenJson.add("rows", new JsonPrimitive(genericContainerScreen.getScreenHandler().getRows()));
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

    public static JsonElement fromItemStack(ItemStack itemStack) {
        JsonObject jsonObject = new JsonObject();

        itemStack.getRegistryEntry().getKey()
                .ifPresent(itemRegistryKey -> jsonObject.addProperty("id", itemRegistryKey.getValue().toString()));

        if (itemStack.getCount() > 0 && !itemStack.getItem().equals(Items.AIR)) {
            jsonObject.addProperty("count", itemStack.getCount());
        }

        DataResult<JsonElement> result = ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, itemStack);
        JsonElement tagElement = result.getOrThrow(true, s -> {
        }).getAsJsonObject().get("tag");

        if (tagElement != null) {
            jsonObject.add("tag", tagElement);

            if (tagElement.getAsJsonObject().has("display")) {
                JsonObject display = tagElement.getAsJsonObject().getAsJsonObject("display");
                tagElement.getAsJsonObject().remove("display");

                if (display.has("Name")) {
                    var nameString = display.getAsJsonPrimitive("Name");
                    jsonObject.add("name", fromText(toText(JsonParser.parseString(nameString.getAsString()))));
                }
                if (display.has("Lore")) {
                    var loreArray = display.getAsJsonArray("Lore");
                    if (!loreArray.isEmpty()) {
                        var lore = new JsonArray();
                        for (JsonElement jsonElement : loreArray) {
                            lore.add(fromText(toText(JsonParser.parseString(jsonElement.getAsString()))));
                        }
                        jsonObject.add("lore", lore);
                    }
                }
            }
        }

        return jsonObject;
    }

    public static JsonElement fromText(Text text) {
        if (Teapilot.flagsManager.isEnabled("EXPERIMENT_TEXT_SERIALIZATION")) {
            JsonElement json = Text.Serializer.toJsonTree(text);
            JsonPrimitive content = new JsonPrimitive(text.getString().replaceAll("§.", ""));

            JsonObject jsonElement = new JsonObject();
            jsonElement.add("json", json);
            jsonElement.add("text", content);

            return jsonElement;
        }
        return Text.Serializer.toJsonTree(text);
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
            textList.add(Text.Serializer.fromJson(jsonElement));
        }
        return textList;
    }

    public static JsonElement fromServerInfo(ServerInfo info) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("address", info.address);
        jsonObject.addProperty("ping", info.ping);
        jsonObject.addProperty("online", info.online);

        return jsonObject;
    }

    public static JsonElement fromEntity(Entity entity) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("type", entity.getType().getRegistryEntry().getKey().get().getValue().toString());
        jsonObject.addProperty("id", entity.getId());
        jsonObject.addProperty("uuid", entity.getUuid().toString());
        jsonObject.add("pos", fromPosition(entity.getPos()));

//        jsonObject.addProperty("invisible", entity.isInvisible());
        jsonObject.add("vel", fromPosition(entity.getVelocity()));

        if (entity.hasCustomName()) {
            jsonObject.add("customName", JsonUtils.fromText(entity.getCustomName()));
        }

        if (entity instanceof LivingEntity livingEntity) {
            JsonArray items = new JsonArray();
            AtomicBoolean hasItem = new AtomicBoolean(false);
            livingEntity.getArmorItems().forEach(itemStack -> {
                items.add(JsonUtils.fromItemStack(itemStack));
                if (!itemStack.getItem().equals(Items.AIR)) hasItem.set(true);
            });
            if (hasItem.get()) jsonObject.add("armor", items);
            if (!livingEntity.getMainHandStack().getItem().equals(Items.AIR))
                jsonObject.add("main_hand", JsonUtils.fromItemStack(livingEntity.getMainHandStack()));
            if (!livingEntity.getOffHandStack().getItem().equals(Items.AIR))
                jsonObject.add("off_hand", JsonUtils.fromItemStack(livingEntity.getOffHandStack()));
        }

        if (entity instanceof ItemEntity itemEntity) {
            jsonObject.add("item", JsonUtils.fromItemStack(itemEntity.getStack()));
        }

        if (entity instanceof Ownable ownable) {
            if (ownable.getOwner() != null) jsonObject.addProperty("owner", ownable.getOwner().getUuid().toString());
        }

        Entity vehicle = entity.getVehicle();
        if (vehicle != null) {
            jsonObject.addProperty("vehicle", vehicle.getUuid().toString());
        }

        if (entity.hasPassengers()) {
            List<Entity> passengerList = entity.getPassengerList();
            JsonArray jsonArray = new JsonArray();
            for (Entity passenger : passengerList) {
                jsonArray.add(passenger.getUuid().toString());
            }
            jsonObject.add("passengers", jsonArray);
        }

        return jsonObject;
    }

    private static JsonElement serializeState(BlockState properties) {
        DataResult<JsonElement> result = BlockState.CODEC.encodeStart(JsonOps.INSTANCE, properties);
        return result.getOrThrow(true, s -> {
        }).getAsJsonObject().get("Properties");
    }

    public static Text toText(JsonElement json) {
        return Text.Serializer.fromJson(json);
    }

    public static BlockPos toBlockPos(JsonElement json) {
        JsonObject positionJson = json.getAsJsonObject();
        int x = positionJson.get("x").getAsInt();
        int y = positionJson.get("y").getAsInt();
        int z = positionJson.get("z").getAsInt();
        return new BlockPos(x, y, z);
    }

    public static Position toPosition(JsonElement json) {
        JsonObject positionJson = json.getAsJsonObject();
        double x = positionJson.get("x").getAsDouble();
        double y = positionJson.get("y").getAsDouble();
        double z = positionJson.get("z").getAsDouble();
        return new PositionImpl(x, y, z);
    }

    public static JsonElement fromSlotItem(SlotItem slotItem) {
        JsonObject slotItemJson = new JsonObject();
        slotItemJson.add("slot", new JsonPrimitive(slotItem.getSlot()));
        slotItemJson.add("item", fromItemStack(slotItem.getItem()));
        return slotItemJson;
    }
}
