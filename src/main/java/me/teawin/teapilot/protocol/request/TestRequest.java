package me.teawin.teapilot.protocol.request;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import org.jetbrains.annotations.Nullable;

public class TestRequest extends Request {
    @Override
    public @Nullable Response call() {
        return null;
    }
}
