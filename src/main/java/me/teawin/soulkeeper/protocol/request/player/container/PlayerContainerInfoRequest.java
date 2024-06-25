package me.teawin.soulkeeper.protocol.request.player.container;

import me.teawin.soulkeeper.protocol.Request;
import me.teawin.soulkeeper.protocol.Response;
import me.teawin.soulkeeper.protocol.response.player.container.PlayerContainerInfoResponse;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;

public class PlayerContainerInfoRequest extends Request {
    @Override
    public @Nullable Response call() throws Exception {
        return new PlayerContainerInfoResponse(MinecraftClient.getInstance().currentScreen);
    }
}
