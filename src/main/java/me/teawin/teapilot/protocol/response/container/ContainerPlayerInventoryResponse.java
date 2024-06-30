package me.teawin.teapilot.protocol.response.container;

import me.teawin.teapilot.protocol.type.SlotItem;
import net.minecraft.text.Text;

import java.util.List;

public class ContainerPlayerInventoryResponse extends ContainerInventoryResponse {

    private List<SlotItem> craft_grid;
    private SlotItem craft_result;
    private List<SlotItem> armor;
    private SlotItem offhand;

    public ContainerPlayerInventoryResponse(String type, Text title, List<SlotItem> craft_grid, SlotItem craft_result, List<SlotItem> armor, SlotItem offhand, List<SlotItem> inventory, List<SlotItem> hotbar) {
        super(type, title, inventory, hotbar);
        this.craft_grid = craft_grid;
        this.craft_result = craft_result;
        this.armor = armor;
        this.offhand = offhand;
    }
}