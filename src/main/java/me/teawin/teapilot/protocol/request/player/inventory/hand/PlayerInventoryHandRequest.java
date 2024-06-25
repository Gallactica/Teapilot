package me.teawin.teapilot.protocol.request.player.inventory.hand;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.protocol.response.player.inventory.hand.PlayerInventoryHandResponse;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;

public class PlayerInventoryHandRequest extends Request {
    @Override
    public @Nullable Response call() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;
        return new PlayerInventoryHandResponse(player.getMainHandStack(), player.getOffHandStack(), player.getInventory().selectedSlot);
    }
}
