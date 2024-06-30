package me.teawin.teapilot.protocol.response.container;

import me.teawin.teapilot.protocol.type.SlotItem;
import net.minecraft.text.Text;

import java.util.List;

public class ContainerChestResponse extends ContainerResponse {

    private List<SlotItem> chest;
    private List<SlotItem> inventory;
    private List<SlotItem> hotbar;

    public ContainerChestResponse(String type, Text title, List<SlotItem> chest, List<SlotItem> inventory, List<SlotItem> hotbar) {
        super(type, title);
        this.chest = chest;
        this.inventory = inventory;
        this.hotbar = hotbar;
    }
}