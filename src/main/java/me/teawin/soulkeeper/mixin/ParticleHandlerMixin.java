package me.teawin.soulkeeper.mixin;

import me.teawin.soulkeeper.Soulkeeper;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public class ParticleHandlerMixin {
    @Inject(at = @At("HEAD"), method = "addParticle(Lnet/minecraft/client/particle/Particle;)V")
    private void addParticle(Particle particle, CallbackInfo ci) {

        Soulkeeper.broadcastParticle(particle);
    }
}
