package me.teawin.soulkeeper.protocol.request;

import me.teawin.soulkeeper.protocol.Request;
import me.teawin.soulkeeper.protocol.Response;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class ChatRequest extends Request {

    @Nullable
    private String text;

    @Nullable
    private Text message;

    @Nullable
    public Text getMessage() {
        if (message != null) return message;
        if (text != null) return Text.of(text);
        return null;
    }

    @Override
    public @Nullable Response call() {
        Text message = this.getMessage();

        assert message != null;
        assert MinecraftClient.getInstance().player != null;

        MinecraftClient.getInstance().player.sendMessage(message);

        return null;
    }
}
