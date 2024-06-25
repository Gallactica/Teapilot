package me.teawin.teapilot.protocol.request.player;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

public class PlayerRotateRequest extends Request {
    private double x;
    private double y;

    @Override
    public @Nullable Response call() throws Exception {
        PlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;
        player.changeLookDirection(x, y);
        return null;
    }
}
