package me.teawin.soulkeeper.protocol.type;

import net.minecraft.item.ItemStack;

public class SlotItem {
    private final int slot;
    private final ItemStack item;

    public SlotItem(int slot, ItemStack item) {
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
