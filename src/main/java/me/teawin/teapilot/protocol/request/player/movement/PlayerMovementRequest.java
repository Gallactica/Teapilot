package me.teawin.teapilot.protocol.request.player.movement;

import me.teawin.teapilot.proposal.MovementOverride;
import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import org.jetbrains.annotations.Nullable;

public class PlayerMovementRequest extends Request {
    int time;
    boolean jumping;
    boolean sneaking;
    boolean sprinting;
    float x;
    float y;

    @Override
    public @Nullable Response call() throws Exception {
        MovementOverride.setMovement(x, y, sneaking, jumping, sprinting, time);
        return null;
    }
}
