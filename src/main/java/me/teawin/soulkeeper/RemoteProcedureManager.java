package me.teawin.soulkeeper;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class RemoteProcedureManager {
    public HashMap<String, Function<JsonObject, JsonObject>> listeners = new HashMap<>();

    public void register(String methodName, Function<JsonObject, JsonObject> consumer) {
        listeners.put(methodName, consumer);
    }

    public Set<String> getMethods() {
        return listeners.keySet();
    }

    public CompletableFuture<@Nullable JsonObject> dispatch(String name, JsonObject jsonObject) {
        var runnable = listeners.get(name);
        if (runnable != null) {
            return CompletableFuture.supplyAsync(() -> runnable.apply(jsonObject));
        } else {
            return CompletableFuture.completedFuture(null);
        }
    }
}
