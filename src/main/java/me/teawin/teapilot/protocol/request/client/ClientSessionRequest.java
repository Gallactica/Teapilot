package me.teawin.teapilot.protocol.request.client;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.session.Session;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ClientSessionRequest extends Request {
    @Override
    public @Nullable Response call() throws Exception {
        return new ClientSessionResponse(MinecraftClient.getInstance()
                .getSession());
    }

    public static class ClientSessionResponse extends Response {
        String username;
        UUID uuid;

        ClientSessionResponse(Session session) {
            username = session.getUsername();
            uuid = session.getUuidOrNull();
        }
    }
}
