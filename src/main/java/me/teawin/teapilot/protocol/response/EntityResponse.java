package me.teawin.teapilot.protocol.response;

import me.teawin.teapilot.protocol.Response;
import net.minecraft.entity.Entity;

public class EntityResponse extends Response {
    private final Entity entity;

    public EntityResponse(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}