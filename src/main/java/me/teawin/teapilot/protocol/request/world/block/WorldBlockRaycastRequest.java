package me.teawin.teapilot.protocol.request.world.block;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.protocol.response.player.RaycastResponse;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.Nullable;

public class WorldBlockRaycastRequest extends Request {
    private static final byte FLAG_PLAYER_FOOT_POS = 1;
    private static final byte FLAG_PLAYER_LOOK = 2;
    private static final byte FLAG_PLAYER_EYE_POS = 4;

    private byte player;
    private Position position;
    private Position target;
    private float yaw;
    private float pitch;
    private double distance;
    private boolean fluids;

    private static Vec3d convertYawAndPitchToVec3d(float yaw, float pitch) {
        float f = pitch * 0.017453292F;
        float g = -yaw * 0.017453292F;
        float h = MathHelper.cos(g);
        float i = MathHelper.sin(g);
        float j = MathHelper.cos(f);
        float k = MathHelper.sin(f);
        return new Vec3d((double) (i * j), (double) (-k), (double) (h * j));
    }

    private static HitResult raycast(RaycastPoints points, boolean includeFluids) {
        assert MinecraftClient.getInstance().world != null;
        assert MinecraftClient.getInstance().player != null;

        RaycastContext raycastContext = new RaycastContext(points.start, points.end, RaycastContext.ShapeType.OUTLINE, includeFluids ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE, MinecraftClient.getInstance().player);

        return MinecraftClient.getInstance().world.raycast(raycastContext);
    }

    public static class RaycastPoints {
        public final Vec3d start;
        public final Vec3d end;

        public RaycastPoints(Vec3d start, Vec3d end) {
            this.start = start;
            this.end = end;
        }

        public static RaycastPoints of(Position position, float yaw, float pitch, double maxDistance) {
            Vec3d start = new Vec3d(position.getX(), position.getY(), position.getZ());
            Vec3d direction = convertYawAndPitchToVec3d(yaw, pitch);
            Vec3d end = start.add(direction.x * maxDistance, direction.y * maxDistance, direction.z * maxDistance);
            return new RaycastPoints(start, end);
        }

        public static RaycastPoints of(Position start, Position end) {
            return new RaycastPoints(new Vec3d(start.getX(), start.getY(), start.getZ()), new Vec3d(end.getX(), end.getY(), end.getZ()));
        }
    }

    @Override
    public @Nullable Response call() throws Exception {
        RaycastResponse response = new RaycastResponse();

        response.setType("MISS");

        if (player != 0) {
            ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;
            assert clientPlayer != null;

            if ((player & FLAG_PLAYER_EYE_POS) == FLAG_PLAYER_EYE_POS) position = clientPlayer.getEyePos();

            if ((player & FLAG_PLAYER_FOOT_POS) == FLAG_PLAYER_FOOT_POS) position = clientPlayer.getEyePos();

            if ((player & FLAG_PLAYER_LOOK) == FLAG_PLAYER_LOOK) {
                yaw = clientPlayer.getYaw();
                pitch = clientPlayer.getPitch();
            }
        }

        assert position != null;
        assert MinecraftClient.getInstance().player != null;

        RaycastPoints points = null;

        if (target != null) {
            points = RaycastPoints.of(position, target);
        } else if (distance > 0) {
            points = RaycastPoints.of(position, yaw, pitch, distance);
        }

        assert points != null;

        HitResult hitResult = raycast(points, fluids);

        if (hitResult instanceof BlockHitResult blockHitResult) {
            BlockState blockState = MinecraftClient.getInstance().player.getWorld().getBlockState(blockHitResult.getBlockPos());

            if (!blockState.getBlock().equals(Blocks.VOID_AIR) && !blockState.getBlock().equals(Blocks.AIR)) {
                response.setBlock(blockState);
                response.setPosition(blockHitResult.getBlockPos());
                response.setType("BLOCK");
            }
        }

        return response;
    }
}
