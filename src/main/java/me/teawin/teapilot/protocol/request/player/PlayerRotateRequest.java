package me.teawin.teapilot.protocol.request.player;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

public class PlayerRotateRequest extends Request {
    private Double x;
    private Double y;

    @Override
    public @Nullable Response call() throws Exception {
        assert x != null;
        assert y != null;

        PlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;
        player.changeLookDirection(x, y);
        return null;
    }
}
