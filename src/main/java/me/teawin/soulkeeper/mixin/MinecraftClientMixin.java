package me.teawin.soulkeeper.mixin;

import me.teawin.soulkeeper.Soulkeeper;
import me.teawin.soulkeeper.SoulkeeperEvents;
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
        var event = SoulkeeperEvents.createEvent(SoulkeeperEvents.WORLD_CHANGE);
        Soulkeeper.soulkeeperServer.broadcast(event);
    }
}
