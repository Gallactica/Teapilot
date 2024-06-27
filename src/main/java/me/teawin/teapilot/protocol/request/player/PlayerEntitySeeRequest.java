package me.teawin.teapilot.protocol.request.player;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.protocol.response.player.PlayerEntitySeeResponse;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;

public class PlayerEntitySeeRequest extends Request {
    private int id;

    @Override
    public @Nullable Response call() throws Exception {
        assert MinecraftClient.getInstance().player != null;
        assert MinecraftClient.getInstance().world != null;
        assert id > 0;

        boolean canSee = MinecraftClient.getInstance().player.canSee(MinecraftClient.getInstance().world.getEntityById(id));

        return new PlayerEntitySeeResponse(canSee);
    }
}
