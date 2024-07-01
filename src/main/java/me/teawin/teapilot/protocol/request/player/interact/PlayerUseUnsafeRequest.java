package me.teawin.teapilot.protocol.request.player.interact;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.mixin.accessor.MinecraftClientAccessor;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;

public class PlayerUseUnsafeRequest extends Request {
    @Override
    public @Nullable Response call() throws Exception {
        var player = MinecraftClient.getInstance().player;
        assert player != null;
        ((MinecraftClientAccessor) MinecraftClient.getInstance()).callDoItemUse();
        return null;
    }
}
