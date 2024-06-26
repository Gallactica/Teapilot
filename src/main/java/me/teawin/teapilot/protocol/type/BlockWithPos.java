package me.teawin.teapilot.protocol.type;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class BlockWithPos {
    private BlockPos pos;
    private BlockState block;

    public BlockWithPos(BlockPos pos, BlockState block) {
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
