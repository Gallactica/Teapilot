package me.teawin.teapilot.mixin;

import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public class ScreenMixin {

    @Inject(method = "shouldPause", at = @At("RETURN"), cancellable = true)
    protected void shouldPause(CallbackInfoReturnable<Boolean> cir) {
        if (((Object) this) instanceof GameMenuScreen) {
            cir.setReturnValue(false);
        }
    }
}
