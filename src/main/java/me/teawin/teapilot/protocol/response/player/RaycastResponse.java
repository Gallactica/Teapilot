package me.teawin.teapilot.protocol.response.player;

import me.teawin.teapilot.protocol.Response;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.PositionImpl;

public class RaycastResponse extends Response {
    private String type;
    private Entity entity;
    private BlockState block;
    private Position position;

    public void setType(String type) {
        this.type = type;
    }

    public void setBlock(BlockState block) {
        this.block = block;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public void setPosition(BlockPos position) {
        this.position = new PositionImpl(position.getX(), position.getY(), position.getZ());
    }

    public void setPosition(Position entityPos) {
        this.position = entityPos;
    }
}
