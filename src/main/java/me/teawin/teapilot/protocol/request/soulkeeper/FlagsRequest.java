package me.teawin.teapilot.protocol.request.teapilot;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.protocol.response.FlagsResponse;
import org.jetbrains.annotations.Nullable;

public class FlagsRequest extends Request {
    @Override
    public @Nullable Response call() {
        return new FlagsResponse();
    }
}
