package me.teawin.teapilot.protocol.request;

import me.teawin.teapilot.Teapilot;
import me.teawin.teapilot.TeapilotEvents;
import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.protocol.response.TeapilotResponse;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TeapilotRequest extends Request {
    @Override
    public @Nullable Response call() throws Exception {
        List<String> methods = new ArrayList<>(Teapilot.manager.getMethods());
        List<String> events = new ArrayList<>(TeapilotEvents.asList());

        List<String> flags = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : Teapilot.flagsManager.flags.entrySet()) {
            flags.add(entry.getKey());
        }

        return new TeapilotResponse(methods, events, flags);
    }
}
