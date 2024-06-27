package me.teawin.teapilot.protocol.request.world;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.protocol.response.player.RaycastResponse;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.Nullable;

public class WorldRaycastRequest extends Request {
    protected static final byte FLAG_PLAYER_FOOT_POS = 1;
    protected static final byte FLAG_PLAYER_LOOK = 2;
    protected static final byte FLAG_PLAYER_EYE_POS = 4;

    protected byte player;
    protected Position position;
    protected Position target;
    protected float yaw;
    protected float pitch;
    protected double distance;
    protected boolean fluids;

    protected static Vec3d convertYawAndPitchToVec3d(float yaw, float pitch) {
        float f = pitch * 0.017453292F;
        float g = -yaw * 0.017453292F;
        float h = MathHelper.cos(g);
        float i = MathHelper.sin(g);
        float j = MathHelper.cos(f);
        float k = MathHelper.sin(f);
        return new Vec3d((double) (i * j), (double) (-k), (double) (h * j));
    }

    protected static double defaultDistance = 10;

    protected static class RaycastPoints {
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

    protected RaycastPoints getPoints() {

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
        } else {
            if (distance == 0) distance = defaultDistance;
            points = RaycastPoints.of(position, yaw, pitch, distance);
        }

        return points;
    }

    @Override
    public @Nullable Response call() throws Exception {
        HitResult hitResult = raycast();

        RaycastResponse response = new RaycastResponse();

        if (!setEntityResult(hitResult, response)) {
            setBlockResult(hitResult, response);
        }

        return response;
    }

    protected boolean setEntityResult(HitResult hitResult, RaycastResponse response) {
        if (hitResult instanceof EntityHitResult entityHitResult) {
            response.setEntity(entityHitResult.getEntity());
            response.setPosition(entityHitResult.getPos());
            response.setType("ENTITY");
            return true;
        }
        return false;
    }

    protected boolean setBlockResult(HitResult hitResult, RaycastResponse response) {
        if (hitResult instanceof BlockHitResult blockHitResult) {
            assert MinecraftClient.getInstance().world != null;
            BlockState blockState = MinecraftClient.getInstance().world.getBlockState(blockHitResult.getBlockPos());

            if (!blockState.getBlock().equals(Blocks.VOID_AIR) && !blockState.getBlock().equals(Blocks.AIR)) {
                response.setBlock(blockState);
                response.setPosition(blockHitResult.getBlockPos());
                response.setType("BLOCK");
                return true;
            }
        }
        return false;
    }

    protected HitResult raycast() {
        RaycastPoints points = getPoints();

        BlockHitResult block = raycastBlock(points);
        EntityHitResult entity = raycastEntity(points);

        if (block != null && entity != null) {

            double entityDst = entity.getEntity().getPos().squaredDistanceTo(points.start) - entity.squaredDistanceTo(entity.getEntity());
            double blockDst = block.getPos().squaredDistanceTo(points.start);

            boolean isEntity = entityDst <= blockDst;

            if (isEntity) {
                return entity;
            } else {
                return block;
            }
        }

        if (block != null) {
            return block;
        }

        return entity;
    }

    protected BlockHitResult raycastBlock(RaycastPoints points) {
        boolean includeFluids = fluids;

        assert MinecraftClient.getInstance().world != null;
        assert MinecraftClient.getInstance().player != null;

        RaycastContext raycastContext = new RaycastContext(points.start, points.end, RaycastContext.ShapeType.OUTLINE, includeFluids ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE, MinecraftClient.getInstance().player);

        return MinecraftClient.getInstance().world.raycast(raycastContext);
    }

    protected EntityHitResult raycastEntity(RaycastPoints points) {
        assert MinecraftClient.getInstance().world != null;
        assert MinecraftClient.getInstance().player != null;

        double dst = points.start.squaredDistanceTo(points.end);

        return ProjectileUtil.raycast(MinecraftClient.getInstance().player, points.start, points.end, new Box(points.start, points.end), Entity::isAlive, dst);
    }
}