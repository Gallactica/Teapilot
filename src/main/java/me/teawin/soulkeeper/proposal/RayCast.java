package me.teawin.soulkeeper.proposal;

import com.google.gson.JsonObject;
import me.teawin.soulkeeper.JsonUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.BlockHitResult;

public class RayCast {
    public static void ray(JsonObject response) {
        assert MinecraftClient.getInstance().player != null;

        response.addProperty("type", "MISS");

        if (MinecraftClient.getInstance().crosshairTarget != null && MinecraftClient.getInstance().crosshairTarget instanceof BlockHitResult blockHitResult) {
            BlockState blockState = MinecraftClient.getInstance().player.getWorld().getBlockState(blockHitResult.getBlockPos());
            response.add("block", JsonUtils.fromBlockState(blockState));
            response.add("block_pos", JsonUtils.fromPosition(blockHitResult.getBlockPos()));
            response.addProperty("type", "BLOCK");
        } else if (MinecraftClient.getInstance().targetedEntity != null) {
            response.add("entity", JsonUtils.fromEntity(MinecraftClient.getInstance().targetedEntity));
            response.add("entity_pos", JsonUtils.fromPosition(MinecraftClient.getInstance().targetedEntity.getPos()));
            response.addProperty("type", "ENTITY");
        }
    }
}
