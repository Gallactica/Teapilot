package me.teawin.soulkeeper.protocol.request.player;

import me.teawin.soulkeeper.protocol.Request;
import me.teawin.soulkeeper.protocol.Response;
import me.teawin.soulkeeper.protocol.response.PlayerInfoResponse;
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
