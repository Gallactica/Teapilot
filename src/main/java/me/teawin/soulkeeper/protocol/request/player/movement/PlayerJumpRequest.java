package me.teawin.soulkeeper.protocol.request.player.movement;

import me.teawin.soulkeeper.protocol.Request;
import me.teawin.soulkeeper.protocol.Response;
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
