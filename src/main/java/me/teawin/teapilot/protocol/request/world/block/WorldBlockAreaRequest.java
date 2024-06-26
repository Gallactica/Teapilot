package me.teawin.teapilot.protocol.request.world.block;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.protocol.response.world.WorldBlockAreaResponse;
import me.teawin.teapilot.protocol.type.BlockWithPos;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WorldBlockAreaRequest extends Request {

    private BlockPos start;
    private BlockPos end;

    @Override
    public @Nullable Response call() {
        var world = MinecraftClient.getInstance().world;
        assert world != null;

        List<BlockWithPos> blocks = new ArrayList<>();

        for (BlockPos blockPos : BlockPos.iterate(start, end)) {
            blocks.add(new BlockWithPos(blockPos.toImmutable(), world.getBlockState(blockPos)));
        }

        return new WorldBlockAreaResponse(blocks);
    }
}
