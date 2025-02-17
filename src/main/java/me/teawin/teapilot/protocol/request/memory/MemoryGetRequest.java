package me.teawin.teapilot.protocol.request.memory;

import com.google.gson.JsonElement;
import me.teawin.teapilot.TeapilotMemory;
import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import org.jetbrains.annotations.Nullable;

public class MemoryGetRequest extends Request {
    private String key;

    @Override
    public @Nullable Response call() throws Exception {
        return new MemoryGetResponse(TeapilotMemory.get(key));
    }

    public static class MemoryGetResponse extends Response {
        public JsonElement value;

        public MemoryGetResponse(@Nullable JsonElement value) {
            super();
            this.value = value;
        }
    }
}
