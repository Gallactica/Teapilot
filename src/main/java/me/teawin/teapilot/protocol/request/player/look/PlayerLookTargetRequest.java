package me.teawin.teapilot.protocol.request.player.look;

import me.teawin.teapilot.proposal.ControlLook;
import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class PlayerLookTargetRequest extends Request {
    private double x;
    private double y;
    private double z;
    private float spread;
    private boolean center;
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

        PlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;

        if (center) {
            x += .5;
            y += .5;
            z += .5;
        }

        Vec3d target = new Vec3d(x, y, z);

        Vec3d vec3d = EntityAnchorArgumentType.EntityAnchor.EYES.positionAt(player);
        double d = target.x - vec3d.x;
        double e = target.y - vec3d.y;
        double f = target.z - vec3d.z;
        double g = Math.sqrt(d * d + f * f);

        float pitch = MathHelper.wrapDegrees((float) (-(MathHelper.atan2(e, g) * 57.2957763671875)));
        float yaw = MathHelper.wrapDegrees((float) (MathHelper.atan2(f, d) * 57.2957763671875) - 90.0F);

        if (duration == 0) {
            duration = 20;
        }
        if (duration < 0) {
            duration = 0;
        }

        ControlLook.look(yaw, pitch, duration);
        ControlLook.spread_pitch = spread_pitch;
        ControlLook.spread_yaw = spread_yaw;
        return null;
    }
}
