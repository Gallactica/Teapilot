package me.teawin.teapilot.protocol.request.player.movement;

import me.teawin.teapilot.proposal.ControlOverride;
import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;

public class PlayerMovementJumpingRequest extends Request {
    private int duration;

    @Override
    public @Nullable Response call() throws Exception {
        ControlOverride.press(MinecraftClient.getInstance().options.jumpKey, duration);
        return null;
    }
}
