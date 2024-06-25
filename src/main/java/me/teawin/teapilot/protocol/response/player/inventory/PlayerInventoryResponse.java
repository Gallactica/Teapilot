package me.teawin.teapilot.protocol.response.player.inventory;

import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.protocol.type.SlotItem;

import java.util.List;

public class PlayerInventoryResponse extends Response {
    private final List<SlotItem> main;
    private final List<SlotItem> hotbar;
    private final List<SlotItem> armor;
    private final SlotItem offHand;
    private final SlotItem mainHand;

    public PlayerInventoryResponse(List<SlotItem> main, List<SlotItem> hotbar, List<SlotItem> armor, SlotItem offHand, SlotItem mainHand) {
        this.main = main;
        this.hotbar = hotbar;
        this.armor = armor;
        this.offHand = offHand;
        this.mainHand = mainHand;
    }

    public List<SlotItem> getMain() {
        return main;
    }

    public List<SlotItem> getHotbar() {
        return hotbar;
    }

    public List<SlotItem> getArmor() {
        return armor;
    }

    public SlotItem getOffHand() {
        return offHand;
    }

    public SlotItem getMainHand() {
        return mainHand;
    }
}
