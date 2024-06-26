package me.teawin.teapilot.protocol.response.world;

import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.protocol.type.BlockWithPos;

import java.util.List;

public class WorldBlockAreaResponse extends Response {
    private List<BlockWithPos> blocks;

    public WorldBlockAreaResponse(List<BlockWithPos> blocks) {
        this.blocks = blocks;
    }
}
