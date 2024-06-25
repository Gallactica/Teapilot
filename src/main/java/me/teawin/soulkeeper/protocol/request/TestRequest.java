package me.teawin.soulkeeper.protocol.request;

import me.teawin.soulkeeper.protocol.Request;
import me.teawin.soulkeeper.protocol.Response;
import org.jetbrains.annotations.Nullable;

public class TestRequest extends Request {
    @Override
    public @Nullable Response call() {
        return null;
    }
}
