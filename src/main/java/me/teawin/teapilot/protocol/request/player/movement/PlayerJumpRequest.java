package me.teawin.teapilot.protocol.request.player.movement;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;

public class PlayerJumpRequest extends Request {
    @Override
    public @Nullable Response call() throws Exception {
        var player = MinecraftClient.getInstance().player;
        assert player != null;
        player.jump();
        return null;
    }
}
