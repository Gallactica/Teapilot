package me.teawin.teapilot.protocol.request.player.movement;

import me.teawin.teapilot.proposal.Movement;
import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import org.jetbrains.annotations.Nullable;

public class PlayerMovementWalkRequest extends Request {
    int duration;
    float x;
    float y;

    @Override
    public @Nullable Response call() throws Exception {
        Movement.setMovement(x, y, duration);
        return null;
    }
}
