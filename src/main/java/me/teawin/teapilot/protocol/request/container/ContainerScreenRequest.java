package me.teawin.teapilot.protocol.request.container;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.protocol.response.container.ContainerScreenResponse;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;

public class ContainerScreenRequest extends Request {
    @Override
    public @Nullable Response call() throws Exception {
        return new ContainerScreenResponse(MinecraftClient.getInstance().currentScreen);
    }
}
