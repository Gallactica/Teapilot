package me.teawin.soulkeeper.protocol.request.player.movement;

import me.teawin.soulkeeper.proposal.MovementOverride;
import me.teawin.soulkeeper.protocol.Request;
import me.teawin.soulkeeper.protocol.Response;
import org.jetbrains.annotations.Nullable;

public class PlayerMovementRequest extends Request {
    int time;
    boolean jumping;
    boolean sneaking;
    float x;
    float y;

    @Override
    public @Nullable Response call() throws Exception {
        MovementOverride.setMovement(x, y, sneaking, jumping, time);
        return null;
    }
}
