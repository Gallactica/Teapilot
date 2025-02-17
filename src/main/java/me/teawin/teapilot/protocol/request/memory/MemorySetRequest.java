package me.teawin.teapilot.protocol.request.memory;

import com.google.gson.JsonElement;
import me.teawin.teapilot.TeapilotMemory;
import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import org.jetbrains.annotations.Nullable;

public class MemorySetRequest extends Request {
    private String key;
    private JsonElement value;

    @Override
    public @Nullable Response call() throws Exception {
        TeapilotMemory.set(key, value);

        return null;
    }
}
