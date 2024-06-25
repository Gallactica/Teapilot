package me.teawin.teapilot.protocol.response;

import com.google.gson.annotations.SerializedName;
import me.teawin.teapilot.protocol.Response;

import java.util.List;

public class TeapilotResponse extends Response {
    @SerializedName("methods")
    private final List<String> methods;
    @SerializedName("events")
    private final List<String> events;
    @SerializedName("flags")
    private final List<String> flags;

    public TeapilotResponse(List<String> methods, List<String> events, List<String> flags) {
        this.methods = methods;
        this.events = events;
        this.flags = flags;
    }
}
