package me.teawin.teapilot.protocol.request.memory;

import me.teawin.teapilot.TeapilotMemory;
import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import org.jetbrains.annotations.Nullable;

public class MemoryDeleteRequest extends Request {
    private String key;

    @Override
    public @Nullable Response call() throws Exception {
        TeapilotMemory.delete(key);

        return null;
    }
}
