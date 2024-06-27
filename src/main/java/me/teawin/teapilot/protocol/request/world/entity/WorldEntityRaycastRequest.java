package me.teawin.teapilot.protocol.request.world.entity;

import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.protocol.request.world.WorldRaycastRequest;
import me.teawin.teapilot.protocol.response.player.RaycastResponse;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class WorldEntityRaycastRequest extends WorldRaycastRequest {
    @Override
    public @Nullable Response call() throws Exception {
        RaycastResponse response = new RaycastResponse();
        HitResult hitResult = raycastEntity(getPoints());
        setEntityResult(hitResult, response);
        return response;
    }
}