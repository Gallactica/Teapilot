package me.teawin.teapilot.protocol.request.teapilot;

import me.teawin.teapilot.Teapilot;
import me.teawin.teapilot.TeapilotEvents;
import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TeapilotRequest extends Request {
    @Override
    public @Nullable Response call() throws Exception {
        List<String> methods = new ArrayList<>(Teapilot.dispatcher.getMethods());
        List<String> events = new ArrayList<>(TeapilotEvents.asList());
        List<String> flags = Teapilot.flags.getAllNames();

        String version = FabricLoader.getInstance()
                .getModContainer("teapilot")
                .orElseThrow()
                .getMetadata()
                .getVersion()
                .getFriendlyString();

        return new TeapilotResponse(methods, events, flags, version);
    }

    public static class TeapilotResponse extends Response {
        private final List<String> methods;
        private final List<String> events;
        private final List<String> flags;
        private final String version;

        public TeapilotResponse(List<String> methods, List<String> events, List<String> flags, String version) {
            this.methods = methods;
            this.events = events;
            this.flags = flags;
            this.version = version;
        }
    }
}
