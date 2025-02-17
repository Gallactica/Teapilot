package me.teawin.teapilot;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TeapilotMemory {

    public static final ConcurrentHashMap<String, JsonElement> memory = new ConcurrentHashMap<>();

    public static @Nullable JsonElement get(String key) {
        return memory.get(key);
    }

    public static void set(String key, JsonElement value) {
        memory.put(key, value);
    }

    public static void delete(String key) {
        memory.remove(key);
    }

    public static List<String> keys() {
        return memory.keySet()
                .stream()
                .toList();
    }

    public static void reset() {
        memory.clear();
    }

    public static int size() {
        return memory.size();
    }
}
