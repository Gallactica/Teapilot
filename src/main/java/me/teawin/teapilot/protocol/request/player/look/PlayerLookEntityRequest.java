package me.teawin.teapilot.protocol.request.player.look;

import me.teawin.teapilot.Teapilot;
import me.teawin.teapilot.proposal.ControlLook;
import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class PlayerLookEntityRequest extends Request {
    private int id;
    private float spread;
    private int duration;

    @Override
    public @Nullable Response call() throws Exception {
        Random random = new Random();

        float spread_pitch = 0;
        float spread_yaw = 0;

        if (spread != 0) {
            spread_pitch = (random.nextFloat() * 2 - 1) * spread / 2;
            spread_yaw = (random.nextFloat() * 2 - 1) * spread;
        }

        if (duration == 0) {
            duration = 20;
        }

        PlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;

        ClientWorld world = MinecraftClient.getInstance().world;
        assert world != null;

        Entity targetEntity = null;

        for (Entity entity : world.getEntities()) {
            if (entity.getId() == this.id) {
                targetEntity = entity;
                break;
            }
        }

        assert targetEntity != null;

        Vec3d vec3d = EntityAnchorArgumentType.EntityAnchor.EYES.positionAt(player);

        Box boundingBox = targetEntity.getBoundingBox();
        double centerX = (boundingBox.minX + boundingBox.maxX) / 2.0;
        double centerY = (boundingBox.minY + boundingBox.maxY) / 2.0;
        double centerZ = (boundingBox.minZ + boundingBox.maxZ) / 2.0;
        Vec3d center = new Vec3d(centerX, centerY, centerZ);

        double d = center.getX() - vec3d.x;
        double e = center.getY() - vec3d.y;
        double f = center.getZ() - vec3d.z;

        double g = Math.sqrt(d * d + f * f);

        var pitch = MathHelper.wrapDegrees((float) (-(MathHelper.atan2(e, g) * 57.2957763671875)));
        var yaw = MathHelper.wrapDegrees((float) (MathHelper.atan2(f, d) * 57.2957763671875) - 90.0F);

        ControlLook.look(yaw, pitch, duration);
        ControlLook.spread_pitch = spread_pitch;
        ControlLook.spread_yaw = spread_yaw;

        return null;
    }
}
