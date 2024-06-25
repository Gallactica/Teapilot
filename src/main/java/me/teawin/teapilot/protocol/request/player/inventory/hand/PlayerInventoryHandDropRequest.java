package me.teawin.teapilot.protocol.request.player.inventory.hand;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;

public class PlayerInventoryHandDropRequest extends Request {
    private boolean stack;

    @Override
    public @Nullable Response call() throws Exception {

        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        assert player != null;

        player.dropSelectedItem(stack);

        return null;
    }
}
