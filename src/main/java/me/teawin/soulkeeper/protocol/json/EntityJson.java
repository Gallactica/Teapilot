package me.teawin.soulkeeper.protocol.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import me.teawin.soulkeeper.JsonUtils;
import net.minecraft.entity.Entity;

import java.lang.reflect.Type;

public class EntityJson implements JsonSerializer<Entity> {
    @Override
    public JsonElement serialize(Entity src, Type typeOfSrc, JsonSerializationContext context) {
        return JsonUtils.fromEntity(src);
    }
}
