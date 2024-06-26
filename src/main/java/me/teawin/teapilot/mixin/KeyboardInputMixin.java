package me.teawin.teapilot.mixin;

import me.teawin.teapilot.proposal.Movement;
import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin {
    @Inject(method = "tick", at = @At(value = "TAIL"))
    public void tick(boolean slowDown, float slowDownFactor, CallbackInfo ci) {
        KeyboardInput input = (KeyboardInput) (Object) this;
        if (Movement.vec.y != 0) {
            input.movementForward = Movement.vec.y > 0 ? 1.0F : -1.0F;
        }
        if (Movement.vec.x != 0) {
            input.movementSideways = Movement.vec.x > 0 ? 1.0F : -1.0F;
        }
        if (Movement.sneaking) input.sneaking = true;
        if (Movement.jumping) input.jumping = true;
        if (slowDown) {
            input.movementSideways *= slowDownFactor;
            input.movementForward *= slowDownFactor;
        }
    }
}
