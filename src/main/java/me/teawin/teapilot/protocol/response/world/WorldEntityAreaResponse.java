package me.teawin.teapilot.protocol.response.world;

import me.teawin.teapilot.protocol.Response;
import net.minecraft.entity.Entity;

import java.util.List;

public class WorldEntityAreaResponse extends Response {
    private List<Entity> entities;

    public WorldEntityAreaResponse(List<Entity> entities) {
        this.entities = entities;
    }
}
