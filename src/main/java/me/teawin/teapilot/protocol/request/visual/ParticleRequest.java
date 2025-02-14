package me.teawin.teapilot.protocol.request.visual;

import me.teawin.teapilot.proposal.ParticleManager;
import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ParticleRequest extends Request {
    private Vec3d position;
    private Vec3d velocity;
    private String type;
    private Boolean relative;

    private static final Vec3d ZERO_VELOCITY = new Vec3d(0, 0, 0);

    @Override
    public @Nullable Response call() throws Exception {
        World world = MinecraftClient.getInstance().world;
        assert world != null;
        assert type != null;

        ParticleType<?> particleType = ParticleManager.typeOf(type);

        if (velocity == null) velocity = ZERO_VELOCITY;

        MinecraftClient.getInstance()
                .execute(() -> {
                    if (relative) {
                        assert MinecraftClient.getInstance().player != null;
                        position = position.add(MinecraftClient.getInstance().player.getPos());
                    }

                    world.addParticle((ParticleEffect) particleType, position.getX(), position.getY(), position.getZ(),
                            velocity.getX(), velocity.getY(), velocity.getZ());
                });

        return null;
    }
}
