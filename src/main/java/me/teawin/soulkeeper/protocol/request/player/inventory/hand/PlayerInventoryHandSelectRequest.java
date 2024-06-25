package me.teawin.soulkeeper.protocol.request.player.inventory.hand;

import me.teawin.soulkeeper.protocol.Request;
import me.teawin.soulkeeper.protocol.Response;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public class PlayerInventoryHandSelectRequest extends Request {
    private int slot;

    @Override
    public @Nullable Response call() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;

        player.getInventory().selectedSlot = MathHelper.clamp(slot, 0, 8);
        return null;
    }
}
