package me.teawin.teapilot.mixin.accessor;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Set;

@Mixin(HandledScreen.class)
public interface HandledScreenAccessor {
    @Accessor
    int getBackgroundWidth();

    @Accessor
    int getBackgroundHeight();

    @Accessor
    int getTitleX();

    @Accessor
    int getTitleY();

    @Accessor
    int getPlayerInventoryTitleX();

    @Accessor
    int getPlayerInventoryTitleY();

    @Accessor
    Text getPlayerInventoryTitle();

    @Accessor
    Slot getFocusedSlot();

    @Accessor
    Slot getTouchDragSlotStart();

    @Accessor
    Slot getTouchDropOriginSlot();

    @Accessor
    Slot getTouchHoveredSlot();

    @Accessor
    Slot getLastClickedSlot();

    @Accessor("x")
    int getX();

    @Accessor("y")
    int getY();

    @Accessor
    boolean isTouchIsRightClickDrag();

    @Accessor
    ItemStack getTouchDragStack();

    @Accessor
    int getTouchDropX();

    @Accessor
    int getTouchDropY();

    @Accessor
    long getTouchDropTime();

    @Accessor
    ItemStack getTouchDropReturningStack();

    @Accessor
    long getTouchDropTimer();

    @Accessor
    Set<Slot> getCursorDragSlots();

    @Accessor
    boolean isCursorDragging();

    @Accessor
    int getHeldButtonType();

    @Accessor
    int getHeldButtonCode();

    @Accessor
    boolean isCancelNextRelease();

    @Accessor
    int getDraggedStackRemainder();

    @Accessor
    long getLastButtonClickTime();

    @Accessor
    int getLastClickedButton();

    @Accessor
    boolean isDoubleClicking();

    @Accessor
    ItemStack getQuickMovingStack();

    @Invoker
    void callOnMouseClick(Slot slot, int slotId, int button, SlotActionType actionType);
}
