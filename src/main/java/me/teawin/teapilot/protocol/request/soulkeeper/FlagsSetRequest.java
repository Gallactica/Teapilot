package me.teawin.teapilot.protocol.request.teapilot;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import org.jetbrains.annotations.Nullable;

import static me.teawin.teapilot.Teapilot.flagsManager;

public class FlagsSetRequest extends Request {
    private boolean state;
    private String name;

    @Override
    public @Nullable Response call() {
        flagsManager.set(name, state);

        return null;
    }
}
