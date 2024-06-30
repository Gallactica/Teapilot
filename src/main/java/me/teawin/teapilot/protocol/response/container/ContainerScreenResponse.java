package me.teawin.teapilot.protocol.response.container;

import me.teawin.teapilot.protocol.Response;
import net.minecraft.client.gui.screen.Screen;

public class ContainerScreenResponse extends Response {
    private final Screen screen;

    public ContainerScreenResponse(Screen screen) {
        this.screen = screen;
    }
}
