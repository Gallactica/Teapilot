package me.teawin.teapilot.protocol.request.sign;

import me.teawin.teapilot.mixin.accessor.AbstractSignEditScreenAccessor;
import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AbstractSignEditScreen;
import org.jetbrains.annotations.Nullable;

public class SignFinishRequest extends Request {
    @Override
    public @Nullable Response call() throws Exception {
        if (MinecraftClient.getInstance().currentScreen instanceof AbstractSignEditScreen signEditScreen) {
            MinecraftClient.getInstance().execute(() -> ((AbstractSignEditScreenAccessor) signEditScreen).callFinishEditing());
        }

        return null;
    }
}
