package me.teawin.teapilot.mixin;

import me.teawin.teapilot.Teapilot;
import net.minecraft.client.Keyboard;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
    private void onKey(long window, int button, int action, int mods, CallbackInfo ci) {
        if (Teapilot.flagsManager.isEnabled("PILOT_CONTROL_ONLY")) ci.cancel();
    }

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void onKey(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (Teapilot.flagsManager.isEnabled("PILOT_CONTROL_ONLY")) ci.cancel();
    }
}
