package me.teawin.teapilot.mixin;

import me.teawin.teapilot.proposal.ControlOverride;
import me.teawin.teapilot.proposal.Movement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.util.PlayerInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin {
    @Inject(method = "tick", at = @At(value = "TAIL"))
    public void tick(boolean slowDown, float slowDownFactor, CallbackInfo ci) {
        KeyboardInput input = (KeyboardInput) (Object) this;

        var settings = MinecraftClient.getInstance().options;

        input.playerInput = new PlayerInput(
                ControlOverride.isPressed(settings.forwardKey) || settings.forwardKey.isPressed(),
                ControlOverride.isPressed(settings.backKey) || settings.backKey.isPressed(),
                ControlOverride.isPressed(settings.leftKey) || settings.leftKey.isPressed(),
                ControlOverride.isPressed(settings.rightKey) || settings.rightKey.isPressed(),
                ControlOverride.isPressed(settings.jumpKey) || settings.jumpKey.isPressed(),
                ControlOverride.isPressed(settings.sneakKey) || settings.sneakKey.isPressed(),
                ControlOverride.isPressed(settings.sprintKey) || settings.sprintKey.isPressed());

        if (Movement.direction.y != 0) {
            input.movementForward = Movement.direction.y > 0 ? 1.0F : -1.0F;
        } else {
            input.movementForward = Movement.getMovementMultiplier(input.playerInput.forward(),
                    input.playerInput.backward());
        }

        if (Movement.direction.x != 0) {
            input.movementSideways = Movement.direction.x > 0 ? 1.0F : -1.0F;
        } else {
            input.movementSideways = Movement.getMovementMultiplier(input.playerInput.left(),
                    input.playerInput.right());
        }

        if (slowDown) {
            input.movementForward *= slowDownFactor;
            input.movementSideways *= slowDownFactor;
        }
    }
}
