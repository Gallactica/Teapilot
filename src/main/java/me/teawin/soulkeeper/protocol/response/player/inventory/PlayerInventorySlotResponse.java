package me.teawin.soulkeeper.protocol.response.player.inventory;

import me.teawin.soulkeeper.protocol.Response;
import net.minecraft.item.ItemStack;

public class PlayerInventorySlotResponse extends Response {
    private final int slot;
    private final ItemStack item;

    public PlayerInventorySlotResponse(int slot, ItemStack item) {
        this.slot = slot;
        this.item = item;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItem() {
        return item;
    }
}
