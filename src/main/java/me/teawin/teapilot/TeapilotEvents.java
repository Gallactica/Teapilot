package me.teawin.teapilot;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public enum TeapilotEvents {
    POSITION("position"),
    PARTICLE("particle"),
    SOUND("sound"),
    CHAT("chat"),
    OVERLAY("overlay"),
    ENTITY_SPAWN("entity_spawn"),
    ENTITY_UPDATE("entity_update"),
    ENTITY_DESPAWN("entity_despawn"),
    TOOLTIP("tooltip"),
    SEND_MESSAGE("send_message"),
    SEND_COMMAND("send_command"),
    DISCONNECT("disconnect"),
    CONNECT("connect"),
    CONTAINER_OPEN("container_open"),
    CONTAINER_CLOSE("container_close"),
    CONTAINER_UPDATE("container_update"),
    CONTAINER_UPDATE_SLOT("container_update_slot"),
    CONTAINER_UPDATE_TRANSACTION("container_update_transaction"),
    BLOCK_UPDATE("block_update"),
    WORLD_CHANGE("world_change"),
    SIGN_OPEN("sign_open"),
    SIGN_CLOSE("sign_close"),
    WINDOW_FOCUS("window_focus"),
    WINDOW_BLUR("window_blur"),
    TICK("tick"),
    FISH_HOOK("fish_hook");

    private final String name;

    TeapilotEvents(final String n) {
        name = n;
    }

    @Override
    public String toString() {
        return name;
    }

    public static List<String> asList() {
        List<String> enumValues = new ArrayList<>();
        for (TeapilotEvents s : TeapilotEvents.values()) {
            enumValues.add(s.toString());
        }
        return enumValues;
    }

    public static JsonObject createEvent(TeapilotEvents event) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("event", event.name);
        return jsonObject;
    }
}
