package me.teawin.teapilot.mixin.accessor;

import net.minecraft.block.WoodType;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.client.gui.screen.ingame.AbstractSignEditScreen;
import net.minecraft.client.util.SelectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractSignEditScreen.class)
public interface AbstractSignEditScreenAccessor {
    @Accessor
    SignBlockEntity getBlockEntity();

    @Accessor
    SignText getText();

    @Accessor
    String[] getMessages();

    @Accessor
    WoodType getSignType();

    @Accessor
    int getCurrentRow();

    @Accessor
    SelectionManager getSelectionManager();

    @Invoker
    boolean callCanEdit();

    @Invoker
    void callSetCurrentRowMessage(String message);

    @Invoker
    void callFinishEditing();

    @Accessor
    void setText(SignText text);

    @Mutable
    @Accessor
    void setMessages(String[] messages);

    @Accessor
    void setCurrentRow(int currentRow);
}
