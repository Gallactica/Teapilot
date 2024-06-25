package me.teawin.soulkeeper.protocol.request.player.look;

import me.teawin.soulkeeper.protocol.Request;
import me.teawin.soulkeeper.protocol.Response;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class PlayerLookRequest extends Request {
    private float yaw;
    private float pitch;
    private float spread;
    private boolean center;

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

        player.setPitch(pitch + spread_pitch);
        player.setYaw(yaw + spread_yaw);
        player.setPitch(MathHelper.clamp(player.getPitch(), -90.0F, 90.0F));

        return null;
    }
}
