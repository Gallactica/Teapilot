package me.teawin.teapilot.protocol.request.client;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import org.jetbrains.annotations.Nullable;

public class ClientDisconnectRequest extends Request {
    @Override
    public @Nullable Response call() throws Exception {
        assert MinecraftClient.getInstance().world != null;

        MinecraftClient.getInstance()
                .execute(() -> {
                    MinecraftClient.getInstance()
                            .disconnect();
                    MinecraftClient.getInstance()
                            .setScreen(new TitleScreen());
                });

        return null;
    }
}
