package me.teawin.teapilot.protocol.request.teapilot;

import me.teawin.teapilot.Teapilot;
import me.teawin.teapilot.TeapilotEvents;
import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.protocol.response.TeapilotResponse;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TeapilotRequest extends Request {
    @Override
    public @Nullable Response call() throws Exception {
        List<String> methods = new ArrayList<>(Teapilot.dispatcher.getMethods());
        List<String> events = new ArrayList<>(TeapilotEvents.asList());
        List<String> flags = Teapilot.flags.getAllNames();

        return new TeapilotResponse(methods, events, flags);
    }
}
