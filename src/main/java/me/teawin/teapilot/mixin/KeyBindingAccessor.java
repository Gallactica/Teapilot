package me.teawin.teapilot.mixin;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyBinding.class)
public interface KeyBindingAccessor {
    @Accessor
    String getTranslationKey();

    @Accessor
    InputUtil.Key getDefaultKey();

    @Accessor
    String getCategory();

    @Accessor
    InputUtil.Key getBoundKey();

    @Accessor
    boolean isPressed();

    @Accessor
    int getTimesPressed();
}
