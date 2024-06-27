package me.teawin.teapilot.protocol.response.player;

import me.teawin.teapilot.protocol.Response;

public class PlayerEntitySeeResponse extends Response {
    private final boolean canSee;

    public PlayerEntitySeeResponse(boolean canSee) {
        this.canSee = canSee;
    }
}
