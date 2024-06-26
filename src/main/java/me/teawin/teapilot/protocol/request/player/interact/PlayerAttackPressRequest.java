package me.teawin.teapilot.protocol.request.player.interact;

import me.teawin.teapilot.proposal.Mouse;
import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;

public class PlayerAttackPressRequest extends Request {
    private int duration;

    @Override
    public @Nullable Response call() throws Exception {
        var player = MinecraftClient.getInstance().player;
        assert player != null;
        Mouse.attackKey.press(duration);
        return null;
    }
}
