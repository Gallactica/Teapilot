package me.teawin.teapilot.protocol.request.container;

import me.teawin.teapilot.mixin.accessor.HandledScreenAccessor;
import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.protocol.response.player.container.PlayerContainerResponse;
import me.teawin.teapilot.protocol.type.SlotItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ContainerRequest extends Request {
    @Override
    public @Nullable Response call() throws Exception {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        if (!(screen instanceof HandledScreen containerScreen)) {
            return new PlayerContainerResponse("empty", null, null, null, null);
        }

        HandledScreenAccessor containerScreenAccessor = (HandledScreenAccessor) screen;

        Text title = containerScreenAccessor.getPlayerInventoryTitle();

        if (screen instanceof GenericContainerScreen genericContainerScreen) {
            int totalChestSlots = genericContainerScreen.getScreenHandler().getRows() * 9;

            List<SlotItem> chest = new ArrayList<>();
            for (int i = 0; i < totalChestSlots; i++) {
                ItemStack slotItem = genericContainerScreen.getScreenHandler().getSlot(i).getStack();
                chest.add(new SlotItem(i, slotItem));
            }

            List<SlotItem> inventory = new ArrayList<>();
            for (int i = 0; i < 27; i++) {
                int slotId = i + totalChestSlots;
                ItemStack slotItem = genericContainerScreen.getScreenHandler().getSlot(slotId).getStack();
                inventory.add(new SlotItem(slotId, slotItem));
            }

            List<SlotItem> hotbar = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                int slotId = i + totalChestSlots + 27;
                ItemStack slotItem = genericContainerScreen.getScreenHandler().getSlot(slotId).getStack();
                hotbar.add(new SlotItem(slotId, slotItem));
            }

            return new PlayerContainerResponse("container", title, chest, inventory, hotbar);
        } else {
            return new PlayerContainerResponse("empty", null, null, null, null);
        }
    }
}
