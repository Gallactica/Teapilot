package me.teawin.soulkeeper.protocol.request.player.inventory;

import me.teawin.soulkeeper.protocol.Request;
import me.teawin.soulkeeper.protocol.Response;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;

public class PlayerInventoryCloseRequest extends Request {
    @Override
    public @Nullable Response call() {
        assert MinecraftClient.getInstance().player != null;

        if (MinecraftClient.getInstance().currentScreen != null) {
            MinecraftClient.getInstance().currentScreen.close();
        }

        return null;
    }
}
