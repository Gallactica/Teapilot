package me.teawin.soulkeeper.mixin;

import me.teawin.soulkeeper.proposal.MovementOverride;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyBinding.class)
public class KeyBindingMixin {
    @Inject(method = "isPressed()Z", at = @At("RETURN"), cancellable = true)
    public void isPressed(CallbackInfoReturnable<Boolean> cir) {
        KeyBinding keyBinding = (KeyBinding) (Object) this;
        if (keyBinding.getTranslationKey().equals("key.sprint") && MovementOverride.sprinting) {
            cir.setReturnValue(true);
        }
    }
}
