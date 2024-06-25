package me.teawin.teapilot.protocol.response;

import me.teawin.teapilot.protocol.Response;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class BlockResponse extends Response {
    private BlockPos pos;
    private BlockState block;

    public BlockResponse(BlockPos pos, BlockState block) {
        this.pos = pos;
        this.block = block;
    }

    public BlockPos getPos() {
        return pos;
    }

    public BlockState getBlock() {
        return block;
    }
}