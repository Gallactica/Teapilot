package me.teawin.soulkeeper.protocol.response.player.container;

import me.teawin.soulkeeper.protocol.Response;
import net.minecraft.client.gui.screen.Screen;

public class PlayerContainerInfoResponse extends Response {
    private final Screen screen;

    public PlayerContainerInfoResponse(Screen screen) {
        this.screen = screen;
    }
}
