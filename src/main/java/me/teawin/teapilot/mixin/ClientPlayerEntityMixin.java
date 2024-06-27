package me.teawin.teapilot.mixin;

import me.teawin.teapilot.Teapilot;
import me.teawin.teapilot.TeapilotEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Inject(method = "closeHandledScreen", at = @At("HEAD"))
    public void closeHandledScreen(CallbackInfo ci) {
        if (Teapilot.flagsManager.isDisabled("PACKET_CONTAINER")) return;
        var screen = MinecraftClient.getInstance().currentScreen;
        if (screen == null) return;
        var event = TeapilotEvents.createEvent(TeapilotEvents.CONTAINER_CLOSE);
        event.addProperty("side", "client");
        Teapilot.teapilotServer.broadcast(event);
    }
}
