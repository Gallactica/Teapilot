package me.teawin.teapilot.protocol.response;

import me.teawin.teapilot.FlagsManager;
import me.teawin.teapilot.flags.NodeProcessor;
import me.teawin.teapilot.protocol.Response;

import java.util.Map;

public class FlagsResponse extends Response {
    final Map<String, Boolean> flags;

    public FlagsResponse() {
        this.flags = NodeProcessor.getKeyValuePairs(FlagsManager.root);
    }
}
