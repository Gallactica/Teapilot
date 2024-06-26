package me.teawin.teapilot.protocol.request.world.block;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.protocol.response.BlockResponse;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class WorldBlockGetRequest extends Request {
    private int x;
    private int y;
    private int z;

    @Override
    public @Nullable Response call() throws Exception {
        assert MinecraftClient.getInstance().player != null;

        BlockPos blockPos = new BlockPos(x, y, z);
        BlockState blockState = MinecraftClient.getInstance().player.getWorld().getBlockState(blockPos);

        return new BlockResponse(blockPos, blockState);
    }
}
