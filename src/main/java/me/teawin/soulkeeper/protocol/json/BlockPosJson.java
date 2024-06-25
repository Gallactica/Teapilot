package me.teawin.soulkeeper.protocol.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import me.teawin.soulkeeper.JsonUtils;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Type;

public class BlockPosJson implements JsonSerializer<BlockPos> {
    @Override
    public JsonElement serialize(BlockPos src, Type typeOfSrc, JsonSerializationContext context) {
        return JsonUtils.fromPosition(src);
    }
}
