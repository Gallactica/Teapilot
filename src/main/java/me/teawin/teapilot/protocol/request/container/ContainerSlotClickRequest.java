package me.teawin.teapilot.protocol.request.container;

import me.teawin.teapilot.proposal.ContainerClick;
import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.jetbrains.annotations.Nullable;

public class ContainerSlotClickRequest extends Request {
    private int slot;
    private int button;
    private boolean stack;
    private boolean shift;

    @Override
    public @Nullable Response call() throws Exception {

        if (MinecraftClient.getInstance().currentScreen instanceof HandledScreen handledScreen) {
            if (slot >= 0)
                ContainerClick.clickSlot(handledScreen.getScreenHandler().getSlot(slot), button, stack, shift);
            else
                ContainerClick.clickSlotOutside(button, stack, shift);
        }

        return null;
    }
}
