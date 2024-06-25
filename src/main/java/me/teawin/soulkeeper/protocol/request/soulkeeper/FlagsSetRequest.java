package me.teawin.soulkeeper.protocol.request.soulkeeper;

import me.teawin.soulkeeper.protocol.Request;
import me.teawin.soulkeeper.protocol.Response;
import org.jetbrains.annotations.Nullable;

import static me.teawin.soulkeeper.Soulkeeper.flagsManager;

public class FlagsSetRequest extends Request {
    private boolean state;
    private String name;

    @Override
    public @Nullable Response call() {
        flagsManager.set(name, state);

        return null;
    }
}
