package me.teawin.teapilot.protocol.request.memory;

import me.teawin.teapilot.TeapilotMemory;
import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MemoryKeysRequest extends Request {
    @Override
    public @Nullable Response call() throws Exception {
        List<String> keys = TeapilotMemory.keys();

        return new MemoryKeysResponse(keys);
    }

    public static class MemoryKeysResponse extends Response {
        public List<String> keys;
        public int count;

        public MemoryKeysResponse(List<String> keys) {
            super();
            this.keys = keys;
            this.count = keys.size();
        }
    }
}
