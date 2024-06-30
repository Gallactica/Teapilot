package me.teawin.teapilot.protocol.response.container;

import me.teawin.teapilot.protocol.type.SlotItem;
import net.minecraft.text.Text;

import java.util.List;

public class ContainerInventoryResponse extends ContainerResponse {

    private List<SlotItem> inventory;
    private List<SlotItem> hotbar;

    public ContainerInventoryResponse(String type, Text title, List<SlotItem> inventory, List<SlotItem> hotbar) {
        super(type, title);
        this.inventory = inventory;
        this.hotbar = hotbar;
    }

    public ContainerInventoryResponse(String type, Text title, List<SlotItem> inventory, List<SlotItem> hotbar, List<SlotItem> slots) {
        super(type, title, slots);
        this.inventory = inventory;
        this.hotbar = hotbar;
    }
}