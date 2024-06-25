package me.teawin.soulkeeper.protocol.request;

import me.teawin.soulkeeper.Soulkeeper;
import me.teawin.soulkeeper.SoulkeeperEvents;
import me.teawin.soulkeeper.protocol.Request;
import me.teawin.soulkeeper.protocol.Response;
import me.teawin.soulkeeper.protocol.response.SoulkeeperResponse;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SoulkeeperRequest extends Request {
    @Override
    public @Nullable Response call() throws Exception {
        List<String> methods = new ArrayList<>(Soulkeeper.manager.getMethods());
        List<String> events = new ArrayList<>(SoulkeeperEvents.asList());

        List<String> flags = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : Soulkeeper.flagsManager.flags.entrySet()) {
            flags.add(entry.getKey());
        }

        return new SoulkeeperResponse(methods, events, flags);
    }
}
