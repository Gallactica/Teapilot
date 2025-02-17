package me.teawin.teapilot.protocol.request.memory;

import me.teawin.teapilot.TeapilotMemory;
import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import org.jetbrains.annotations.Nullable;

public class MemorySizeRequest extends Request {
    @Override
    public @Nullable Response call() throws Exception {
        return new MemorySizeResponse(TeapilotMemory.size());
    }

    public static class MemorySizeResponse extends Response {
        public int size;

        public MemorySizeResponse(int value) {
            super();
            this.size = value;
        }
    }
}
