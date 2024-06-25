package me.teawin.soulkeeper.protocol.response;

import me.teawin.soulkeeper.protocol.Response;

import java.util.Map;

import static me.teawin.soulkeeper.Soulkeeper.flagsManager;

public class FlagsResponse extends Response {
    final Map<String, Boolean> flags;

    public FlagsResponse() {
        this.flags = flagsManager.flags;
    }
}
