package me.teawin.teapilot.protocol.request.player;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.protocol.response.PlayerInfoResponse;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.projectile.FishingBobberEntity;
import org.jetbrains.annotations.Nullable;

public class PlayerInfoRequest extends Request {
    @Override
    public @Nullable Response call() throws Exception {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;
        HungerManager hungerManager = player.getHungerManager();
        FishingBobberEntity fishHook = player.fishHook;
        return new PlayerInfoResponse(
                player.getId(),
                player.getUuid().toString(),
                player.getInventory().selectedSlot,
                player.getHealth(),
                hungerManager.getFoodLevel(),
                hungerManager.getSaturationLevel(),
                player.experienceLevel,
                player.totalExperience,
                fishHook
        );
    }
}
