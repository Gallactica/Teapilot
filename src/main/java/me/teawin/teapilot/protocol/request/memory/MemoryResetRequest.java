package me.teawin.teapilot.protocol.request.memory;

import me.teawin.teapilot.TeapilotMemory;
import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import org.jetbrains.annotations.Nullable;

public class MemoryResetRequest extends Request {
    @Override
    public @Nullable Response call() throws Exception {
        TeapilotMemory.reset();

        return null;
    }
}
