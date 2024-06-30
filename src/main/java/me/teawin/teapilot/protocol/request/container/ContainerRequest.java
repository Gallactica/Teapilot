package me.teawin.teapilot.protocol.request.container;

import me.teawin.teapilot.mixin.accessor.HandledScreenAccessor;
import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.protocol.response.container.ContainerChestResponse;
import me.teawin.teapilot.protocol.response.container.ContainerInventoryResponse;
import me.teawin.teapilot.protocol.response.container.ContainerPlayerInventoryResponse;
import me.teawin.teapilot.protocol.response.container.ContainerResponse;
import me.teawin.teapilot.protocol.type.SlotItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ContainerRequest extends Request {
    @Override
    public @Nullable Response call() throws Exception {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        if (!(screen instanceof HandledScreen containerScreen)) {
            return new ContainerResponse("container.empty", null);
        }

        if (screen instanceof CreativeInventoryScreen) {
            return new ContainerResponse("container.creative", null);
        }

        HandledScreenAccessor containerScreenAccessor = (HandledScreenAccessor) screen;

        String type = ((TranslatableTextContent) containerScreen.getTitle().getContent()).getKey();

        Text title = containerScreenAccessor.getPlayerInventoryTitle();

        if (screen instanceof GenericContainerScreen genericContainerScreen) {
            int chestOffset = genericContainerScreen.getScreenHandler().getRows() * 9;

            List<SlotItem> chest = getSlots(genericContainerScreen, 0, chestOffset);
            List<SlotItem> inventory = getSlots(genericContainerScreen, chestOffset, 27);
            List<SlotItem> hotbar = getSlots(genericContainerScreen, chestOffset + 27, 9);

            return new ContainerChestResponse(type, title, chest, inventory, hotbar);
        } else if (screen instanceof InventoryScreen craftingScreen) {
            List<SlotItem> craftResult = getSlots(craftingScreen, 0, 1);
            List<SlotItem> craftGrid = getSlots(craftingScreen, 1, 4);
            List<SlotItem> armor = getSlots(craftingScreen, 5, 4);
            List<SlotItem> inventory = getSlots(craftingScreen, 9, 27);
            List<SlotItem> hotbar = getSlots(craftingScreen, 36, 9);
            List<SlotItem> offhand = getSlots(craftingScreen, 0, 1);

            return new ContainerPlayerInventoryResponse(type, title, craftGrid, craftResult.get(0), armor, offhand.get(0), inventory, hotbar);
        }

        System.out.println(containerScreen.getClass().getSimpleName());

        int unknownSlotsSize = containerScreen.getScreenHandler().slots.size();

        if (unknownSlotsSize > 36) {
            unknownSlotsSize -= 36;
            List<SlotItem> slots = getSlots(containerScreen, 0, unknownSlotsSize);
            List<SlotItem> inventory = getSlots(containerScreen, unknownSlotsSize, 27);
            List<SlotItem> hotbar = getSlots(containerScreen, unknownSlotsSize + 27, 9);
            return new ContainerInventoryResponse(type, containerScreen.getTitle(), inventory, hotbar, slots);
        }

        var slots = getSlots(containerScreen, 0, unknownSlotsSize);
        return new ContainerResponse(type, containerScreen.getTitle(), slots);
    }

    static List<SlotItem> getSlots(HandledScreen<?> handledScreen, int start, int size) {
        var handler = handledScreen.getScreenHandler();
        List<SlotItem> slots = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int slotId = i + start;
            ItemStack slotItem = handler.getSlot(slotId).getStack();
            slots.add(new SlotItem(slotId, slotItem));
        }
        return slots;
    }
}
