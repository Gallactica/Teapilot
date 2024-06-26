package me.teawin.teapilot.protocol.json;

import com.google.gson.*;
import me.teawin.teapilot.JsonUtils;
import net.minecraft.util.math.Position;

import java.lang.reflect.Type;

public class PositionJson implements JsonSerializer<Position>, JsonDeserializer<Position> {
    @Override
    public JsonElement serialize(Position src, Type typeOfSrc, JsonSerializationContext context) {
        return JsonUtils.fromPosition(src);
    }

    @Override
    public Position deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return JsonUtils.toPosition(json);
    }
}
