package me.teawin.teapilot.protocol.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import me.teawin.teapilot.JsonUtils;
import net.minecraft.client.gui.hud.ClientBossBar;

import java.lang.reflect.Type;

public class BossBarJson implements JsonSerializer<ClientBossBar> {

    @Override
    public JsonElement serialize(ClientBossBar src, Type typeOfSrc, JsonSerializationContext context) {
        var bar = new JsonObject();

        bar.addProperty("uuid", src.getUuid()
                .toString());
        bar.add("text", JsonUtils.fromText(src.getName()));
        bar.addProperty("progress", src.getPercent());
        bar.addProperty("color", src.getColor()
                .toString());
        bar.addProperty("style", src.getStyle()
                .toString());

        return bar;
    }
}

