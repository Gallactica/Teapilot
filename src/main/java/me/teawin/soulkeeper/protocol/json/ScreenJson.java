package me.teawin.soulkeeper.protocol.json;

import com.google.gson.*;
import me.teawin.soulkeeper.JsonUtils;
import net.minecraft.client.gui.screen.Screen;

import java.lang.reflect.Type;

public class ScreenJson implements JsonSerializer<Screen> {
    @Override
    public JsonElement serialize(Screen src, Type typeOfSrc, JsonSerializationContext context) {
        return JsonUtils.fromScreen(src);
    }

}
