package me.teawin.soulkeeper;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public enum SoulkeeperEvents {
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
    BLOCK_UPDATE("block_update"),
    WORLD_CHANGE("world_change"),
    FISH_HOOK("fish_hook");

    private final String name;

    SoulkeeperEvents(final String n) {
        name = n;
    }

    @Override
    public String toString() {
        return name;
    }

    public static List<String> asList() {
        List<String> enumValues = new ArrayList<>();
        for (SoulkeeperEvents s : SoulkeeperEvents.values()) {
            enumValues.add(s.toString());
        }
        return enumValues;
    }

    public static JsonObject createEvent(SoulkeeperEvents event) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("event", event.name);
        return jsonObject;
    }
}
