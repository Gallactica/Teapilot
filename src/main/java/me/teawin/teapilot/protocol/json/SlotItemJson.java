package me.teawin.teapilot.protocol.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import me.teawin.teapilot.JsonUtils;
import me.teawin.teapilot.protocol.type.SlotItem;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Type;
import java.util.List;

public class SlotItemJson implements JsonSerializer<SlotItem> {
    @Override
    public JsonElement serialize(SlotItem src, Type typeOfSrc, JsonSerializationContext context) {
        var array = new JsonArray();
        array.add(context.serialize(src.getSlot()));
        array.add(context.serialize(src.getItem()));
        return array;
    }
}
