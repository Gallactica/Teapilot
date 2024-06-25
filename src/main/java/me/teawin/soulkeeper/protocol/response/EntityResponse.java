package me.teawin.soulkeeper.protocol.response;

import me.teawin.soulkeeper.protocol.Response;
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