package me.teawin.teapilot.protocol.response.player.container;

import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.protocol.type.SlotItem;
import net.minecraft.text.Text;


import java.util.List;

public class PlayerContainerResponse extends Response {
    private String type;
    private Text title;
    private List<SlotItem> chest;
    private List<SlotItem> inventory;
    private List<SlotItem> hotbar;

    public PlayerContainerResponse(String type, Text title, List<SlotItem> chest, List<SlotItem> inventory, List<SlotItem> hotbar) {
        this.type = type;
        this.title = title;
        this.chest = chest;
        this.inventory = inventory;
        this.hotbar = hotbar;
    }

    public String getType() {
        return type;
    }

    public Text getTitle() {
        return title;
    }

    public List<SlotItem> getChest() {
        return chest;
    }

    public List<SlotItem> getInventory() {
        return inventory;
    }

    public List<SlotItem> getHotbar() {
        return hotbar;
    }
}