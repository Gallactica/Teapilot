package me.teawin.teapilot.protocol.response;

import me.teawin.teapilot.protocol.Response;

import java.util.Map;

import static me.teawin.teapilot.Teapilot.flagsManager;

public class FlagsResponse extends Response {
    final Map<String, Boolean> flags;

    public FlagsResponse() {
        this.flags = flagsManager.flags;
    }
}
