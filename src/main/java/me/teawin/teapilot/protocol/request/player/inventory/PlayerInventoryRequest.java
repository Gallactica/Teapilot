package me.teawin.teapilot.protocol.request.player.inventory;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.protocol.response.player.inventory.PlayerInventoryResponse;
import me.teawin.teapilot.protocol.type.SlotItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PlayerInventoryRequest extends Request {
    @Override
    public @Nullable Response call() throws Exception {
        assert MinecraftClient.getInstance().player != null;

        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        List<SlotItem> main = new ArrayList<>();
        for (int i = 0; i < 27; i++) {
            int slotId = i + 9;
            ItemStack slotItem = player.getInventory().getStack(slotId);
            main.add(new SlotItem(slotId, slotItem));
        }

        List<SlotItem> hotbar = new ArrayList<>();
        for (int slotId = 0; slotId < 9; slotId++) {
            ItemStack slotItem = player.getInventory().getStack(slotId);
            hotbar.add(new SlotItem(slotId, slotItem));
        }

        List<SlotItem> armor = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int slotId = i + 36;
            ItemStack slotItem = player.getInventory().getStack(slotId);
            armor.add(new SlotItem(slotId, slotItem));
        }

        SlotItem offHand = new SlotItem(40, player.getInventory().getStack(40));

        SlotItem mainHand = new SlotItem(player.getInventory().selectedSlot, player.getInventory().getStack(player.getInventory().selectedSlot));

        return new PlayerInventoryResponse(main, hotbar, armor, offHand, mainHand);
    }
}
