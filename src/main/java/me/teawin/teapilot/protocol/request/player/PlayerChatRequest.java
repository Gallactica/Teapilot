package me.teawin.teapilot.protocol.request.player;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static me.teawin.teapilot.Teapilot.flags;

public class PlayerChatRequest extends Request {
    private String text;
    private String command;

    private @Nullable String getMessage() {
        String value = null;
        if (text != null) value = text;
        if (command != null) value = command;
        if (value != null) value = value.trim();
        return value;
    }

    @Override
    public @Nullable Response call() throws Exception {
        String message = getMessage();

        assert message != null;

        if (message.startsWith("/")) {
            var prev = flags.isEnabled("INTERCEPT_SEND_CHAT_COMMAND");
            flags.toggle("INTERCEPT_SEND_CHAT_COMMAND", false);
            Objects.requireNonNull(MinecraftClient.getInstance()
                            .getNetworkHandler())
                    .sendChatCommand(message.substring(1));
            flags.toggle("INTERCEPT_SEND_CHAT_COMMAND", prev);
        } else {
            var prev = flags.isEnabled("INTERCEPT_SEND_CHAT_MESSAGE");
            flags.toggle("INTERCEPT_SEND_CHAT_MESSAGE", false);
            Objects.requireNonNull(MinecraftClient.getInstance()
                            .getNetworkHandler())
                    .sendChatMessage(message);
            flags.toggle("INTERCEPT_SEND_CHAT_MESSAGE", prev);
        }

        return null;
    }
}
