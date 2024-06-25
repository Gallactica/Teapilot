package me.teawin.teapilot.mixin;

import me.teawin.teapilot.Teapilot;
import me.teawin.teapilot.TeapilotEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "joinWorld", at = @At("TAIL"))
    public void joinWorld(ClientWorld world, CallbackInfo ci) {
        var event = TeapilotEvents.createEvent(TeapilotEvents.WORLD_CHANGE);
        Teapilot.teapilotServer.broadcast(event);
    }
}
