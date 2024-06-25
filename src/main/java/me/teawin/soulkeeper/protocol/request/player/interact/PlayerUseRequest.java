package me.teawin.soulkeeper.protocol.request.player.interact;

import me.teawin.soulkeeper.protocol.Request;
import me.teawin.soulkeeper.protocol.Response;
import me.teawin.soulkeeper.mixin.MinecraftClientAccessor;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;

public class PlayerUseRequest extends Request {
    @Override
    public @Nullable Response call() throws Exception {
        var player = MinecraftClient.getInstance().player;
        assert player != null;
        ((MinecraftClientAccessor) MinecraftClient.getInstance()).callDoItemUse();
        return null;
    }
}
