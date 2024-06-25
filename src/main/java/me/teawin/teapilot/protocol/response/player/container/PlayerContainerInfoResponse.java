package me.teawin.teapilot.protocol.response.player.container;

import me.teawin.teapilot.protocol.Response;
import net.minecraft.client.gui.screen.Screen;

public class PlayerContainerInfoResponse extends Response {
    private final Screen screen;

    public PlayerContainerInfoResponse(Screen screen) {
        this.screen = screen;
    }
}
