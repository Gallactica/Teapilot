package me.teawin.soulkeeper.protocol.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import me.teawin.soulkeeper.JsonUtils;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Type;

public class ItemStackJson implements JsonSerializer<ItemStack> {
    @Override
    public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
        return JsonUtils.fromItemStack(src);
    }
}
