package me.teawin.teapilot.protocol.request.world.entity;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.protocol.response.world.WorldEntityAreaResponse;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class WorldEntityAreaRequest extends Request {

    private Vec3d position;
    private int radius;
    private boolean relative;
    private Vec3d start;
    private Vec3d end;

    @Override
    public @Nullable Response call() {
        var world = MinecraftClient.getInstance().world;
        assert world != null;

        if (relative) {
            assert MinecraftClient.getInstance().player != null;
            position = Vec3d.of(MinecraftClient.getInstance().player.getBlockPos());
        }

        if (position != null) {
            if (radius > 0) {
                start = new Vec3d(-radius, -radius, -radius);
                end = new Vec3d(radius, radius, radius);
            }
            start = start.add(position);
            end = end.add(position);
        }

        assert start != null;
        assert end != null;

        List<Entity> targetEntities = world.getEntitiesByClass(Entity.class, new Box(start, end),
                Predicate.not(Predicate.isEqual(MinecraftClient.getInstance().player)));

        return new WorldEntityAreaResponse(targetEntities);
    }
}
