package me.teawin.teapilot.protocol.request.player;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.protocol.response.player.RaycastResponse;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class PlayerRaycastRequest extends Request {
    @Override
    public @Nullable Response call() {
        RaycastResponse response = new RaycastResponse();

        assert MinecraftClient.getInstance().player != null;

        response.setType("MISS");

        if (MinecraftClient.getInstance().crosshairTarget != null && MinecraftClient.getInstance().crosshairTarget instanceof BlockHitResult blockHitResult) {
            BlockState blockState = MinecraftClient.getInstance().player.getWorld().getBlockState(blockHitResult.getBlockPos());
            response.setBlock(blockState);
            response.setPosition(blockHitResult.getBlockPos());
            response.setType("BLOCK");
        } else if (MinecraftClient.getInstance().targetedEntity != null) {
            response.setEntity(MinecraftClient.getInstance().targetedEntity);
            response.setPosition(MinecraftClient.getInstance().targetedEntity.getPos());
            response.setType("ENTITY");
        }

        return response;
    }
}
