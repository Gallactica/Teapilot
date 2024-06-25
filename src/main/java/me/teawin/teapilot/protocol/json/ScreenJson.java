package me.teawin.teapilot.protocol.json;

import com.google.gson.*;
import me.teawin.teapilot.JsonUtils;
import net.minecraft.client.gui.screen.Screen;

import java.lang.reflect.Type;

public class ScreenJson implements JsonSerializer<Screen> {
    @Override
    public JsonElement serialize(Screen src, Type typeOfSrc, JsonSerializationContext context) {
        return JsonUtils.fromScreen(src);
    }

}
