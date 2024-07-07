package me.teawin.teapilot.protocol.request.visual;

import me.teawin.teapilot.proposal.ParticleManager;
import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.PositionImpl;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ParticleRequest extends Request {
    private Position position;
    private Position velocity;
    private String type;

    private static final Position ZERO_VELOCITY = new PositionImpl(0, 0, 0);

    @Override
    public @Nullable Response call() throws Exception {
        World world = MinecraftClient.getInstance().world;
        assert world != null;
        assert type != null;

        ParticleType<?> particleType = ParticleManager.typeOf(type);

        if (velocity == null) velocity = ZERO_VELOCITY;

        MinecraftClient.getInstance().execute(() -> {
            world.addParticle((ParticleEffect) particleType, position.getX(), position.getY(), position.getZ(),
                    velocity.getX(), velocity.getY(), velocity.getZ());
        });

        return null;
    }
}
