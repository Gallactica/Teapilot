package me.teawin.teapilot.protocol.request.player.movement;

import me.teawin.teapilot.proposal.Movement;
import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import org.jetbrains.annotations.Nullable;

public class PlayerMovementRequest extends Request {
    private int duration;
    private boolean jumping;
    private boolean sneaking;
    private boolean sprinting;
    private float x;
    private float y;

    @Override
    public @Nullable Response call() throws Exception {
        Movement.setMovement(x, y, sneaking, jumping, sprinting, duration);
        return null;
    }
}
