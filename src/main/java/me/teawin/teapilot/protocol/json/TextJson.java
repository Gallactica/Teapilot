package me.teawin.teapilot.protocol.json;

import com.google.gson.*;
import me.teawin.teapilot.JsonUtils;
import net.minecraft.text.Text;

import java.lang.reflect.Type;

public class TextJson implements JsonSerializer<Text>, JsonDeserializer<Text> {
    @Override
    public JsonElement serialize(Text src, Type typeOfSrc, JsonSerializationContext context) {
        return JsonUtils.fromText(src);
    }

    @Override
    public Text deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return JsonUtils.toText(json);
    }
}
