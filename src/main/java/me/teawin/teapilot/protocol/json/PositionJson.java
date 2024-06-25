package me.teawin.teapilot.protocol.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import me.teawin.teapilot.JsonUtils;
import net.minecraft.util.math.Position;

import java.lang.reflect.Type;

public class PositionJson implements JsonSerializer<Position> {
    @Override
    public JsonElement serialize(Position src, Type typeOfSrc, JsonSerializationContext context) {
        return JsonUtils.fromPosition(src);
    }
}
