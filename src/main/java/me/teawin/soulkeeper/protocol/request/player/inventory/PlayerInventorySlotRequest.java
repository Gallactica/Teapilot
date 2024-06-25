package me.teawin.soulkeeper.protocol.request.player.inventory;

import me.teawin.soulkeeper.protocol.Request;
import me.teawin.soulkeeper.protocol.Response;
import me.teawin.soulkeeper.protocol.response.player.inventory.PlayerInventorySlotResponse;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class PlayerInventorySlotRequest extends Request {
    private int slot;

    @Override
    public @Nullable Response call() throws Exception {
        assert MinecraftClient.getInstance().player != null;

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        ItemStack itemStack = player.getInventory().getStack(slot);

        return new PlayerInventorySlotResponse(slot, itemStack);
    }
}
