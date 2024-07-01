package me.teawin.teapilot.mixin;

import me.teawin.teapilot.proposal.ControlOverride;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(KeyBinding.class)
public class KeyBindingMixin {
    @Shadow
    @Final
    private static Map<InputUtil.Key, KeyBinding> KEY_TO_BINDINGS;

    @Inject(method = "setKeyPressed", at = @At("HEAD"), cancellable = true)
    private static void setKeyPressed(InputUtil.Key key, boolean pressed, CallbackInfo ci) {
        ci.cancel();

        KeyBinding keyBinding = KEY_TO_BINDINGS.get(key);
        if (keyBinding != null) {
            keyBinding.setPressed(ControlOverride.isPressed(key) || pressed);
        }
    }
}
