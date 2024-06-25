package me.teawin.soulkeeper.mixin;

import me.teawin.soulkeeper.Soulkeeper;
import me.teawin.soulkeeper.SoulkeeperEvents;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Inject(method = "closeHandledScreen", at = @At("TAIL"))
    public void closeHandledScreen(CallbackInfo ci) {
        if (Soulkeeper.flagsManager.isDisabled("PACKET_CONTAINER")) return;
        var event = SoulkeeperEvents.createEvent(SoulkeeperEvents.CONTAINER_CLOSE);
        Soulkeeper.soulkeeperServer.broadcast(event);
    }
}
