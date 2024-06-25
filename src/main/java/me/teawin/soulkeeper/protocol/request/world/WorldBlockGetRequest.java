package me.teawin.soulkeeper.protocol.request.world;

import com.google.gson.JsonObject;
import me.teawin.soulkeeper.JsonUtils;
import me.teawin.soulkeeper.protocol.Request;
import me.teawin.soulkeeper.protocol.Response;
import me.teawin.soulkeeper.protocol.response.BlockResponse;
import me.teawin.soulkeeper.protocol.response.EntityResponse;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

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
