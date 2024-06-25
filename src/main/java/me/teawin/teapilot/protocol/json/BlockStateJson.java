package me.teawin.teapilot.protocol.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import me.teawin.teapilot.JsonUtils;
import net.minecraft.block.BlockState;

import java.lang.reflect.Type;

public class BlockStateJson implements JsonSerializer<BlockState> {
    @Override
    public JsonElement serialize(BlockState src, Type typeOfSrc, JsonSerializationContext context) {
        return JsonUtils.fromBlockState(src);
    }
}
