package me.teawin.teapilot.protocol.request.sign;

import me.teawin.teapilot.mixin.AbstractSignEditScreenAccessor;
import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AbstractSignEditScreen;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public class SignEditRequest extends Request {
    private int row;
    private String message;

    @Override
    public @Nullable Response call() throws Exception {
        if (MinecraftClient.getInstance().currentScreen instanceof AbstractSignEditScreen signEditScreen) {
            int rowId = MathHelper.clamp(row, 0, 3);

            AbstractSignEditScreenAccessor editScreenAccessor = (AbstractSignEditScreenAccessor) signEditScreen;
            int maxWidth = ((AbstractSignEditScreenAccessor) signEditScreen).getBlockEntity().getMaxTextWidth();
            
            String lastMessage = "";
            for (char c : message.toCharArray()) {
                lastMessage += c;
                int width = MinecraftClient.getInstance().textRenderer.getWidth(lastMessage);
                if (width > maxWidth) {
                    lastMessage = lastMessage.substring(0, lastMessage.length() - 1);
                    break;
                }
            }

            editScreenAccessor.setCurrentRow(rowId);
            editScreenAccessor.callSetCurrentRowMessage(lastMessage);
        }

        return null;
    }
}
