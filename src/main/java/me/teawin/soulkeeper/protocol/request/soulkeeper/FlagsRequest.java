package me.teawin.soulkeeper.protocol.request.soulkeeper;

import me.teawin.soulkeeper.protocol.Request;
import me.teawin.soulkeeper.protocol.Response;
import me.teawin.soulkeeper.protocol.response.FlagsResponse;
import org.jetbrains.annotations.Nullable;

public class FlagsRequest extends Request {
    @Override
    public @Nullable Response call() {
        return new FlagsResponse();
    }
}
