package me.teawin.soulkeeper.mixin;

import me.teawin.soulkeeper.proposal.MovementOverride;
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
        if (MovementOverride.vec.y != 0) {
            input.movementForward = MovementOverride.vec.y > 0 ? 1.0F : -1.0F;
        }
        if (MovementOverride.vec.x != 0) {
            input.movementSideways = MovementOverride.vec.x > 0 ? 1.0F : -1.0F;
        }
        if (MovementOverride.sneaking) input.sneaking = true;
        if (MovementOverride.jumping) input.jumping = true;
        if (slowDown) {
            input.movementSideways *= slowDownFactor;
            input.movementForward *= slowDownFactor;
        }
    }
}
