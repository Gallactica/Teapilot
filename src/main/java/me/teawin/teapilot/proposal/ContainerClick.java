package me.teawin.teapilot.proposal;

import me.teawin.teapilot.mixin.HandledScreenAccessor;
import me.teawin.teapilot.mixin.SlotAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class ContainerClick {

    public static int SLOT_OUTSIDE = -999;

    public static int BUTTON_THROW = -1;
    public static int BUTTON_LEFT = 0;
    public static int BUTTON_RIGHT = 1;

    public static void clickSlot(Slot slot, int index, int button, boolean stack, boolean shift) {
        if (MinecraftClient.getInstance().currentScreen != null) {
            int buttonType = BUTTON_THROW == button || index == SLOT_OUTSIDE ? index == SLOT_OUTSIDE ? (stack ? BUTTON_LEFT : BUTTON_RIGHT) : (stack ? BUTTON_RIGHT : BUTTON_LEFT) : button;

            SlotActionType actionType = BUTTON_THROW == button ? index == SLOT_OUTSIDE ? SlotActionType.PICKUP : SlotActionType.THROW : shift ? SlotActionType.QUICK_MOVE : SlotActionType.PICKUP;

            MinecraftClient.getInstance().execute(() -> {
                HandledScreenAccessor screen = (HandledScreenAccessor) MinecraftClient.getInstance().currentScreen;
                screen.callOnMouseClick(slot, index, buttonType, actionType);
            });
        }
    }

    public static void clickSlot(Slot slot, int button, boolean stack, boolean shift) {
        clickSlot(slot, ((SlotAccessor) slot).getIndex(), button, stack, shift);
    }

    public static void clickSlotOutside(int button, boolean stack, boolean shift) {
        clickSlot((Slot) null, SLOT_OUTSIDE, button, stack, shift);
    }
}
