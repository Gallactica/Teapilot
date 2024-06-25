package me.teawin.soulkeeper.protocol.response.player.inventory.hand;

import me.teawin.soulkeeper.protocol.Response;
import net.minecraft.item.ItemStack;

public class PlayerInventoryHandResponse extends Response {
    private final ItemStack mainHand;
    private final ItemStack offHand;
    private final int slot;

    public PlayerInventoryHandResponse(ItemStack mainHand, ItemStack offHand, int slot) {
        this.mainHand = mainHand;
        this.offHand = offHand;
        this.slot = slot;
    }

    public ItemStack getMainHand() {
        return mainHand;
    }

    public ItemStack getOffHand() {
        return offHand;
    }

    public int getSlot() {
        return slot;
    }
}