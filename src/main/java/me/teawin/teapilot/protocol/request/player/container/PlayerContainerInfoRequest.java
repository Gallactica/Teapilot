package me.teawin.teapilot.protocol.request.player.container;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.protocol.response.player.container.PlayerContainerInfoResponse;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;

public class PlayerContainerInfoRequest extends Request {
    @Override
    public @Nullable Response call() throws Exception {
        return new PlayerContainerInfoResponse(MinecraftClient.getInstance().currentScreen);
    }
}
