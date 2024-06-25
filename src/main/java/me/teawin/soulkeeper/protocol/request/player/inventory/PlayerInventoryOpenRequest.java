package me.teawin.soulkeeper.protocol.request.player.inventory;

import me.teawin.soulkeeper.protocol.Request;
import me.teawin.soulkeeper.protocol.Response;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import org.jetbrains.annotations.Nullable;

public class PlayerInventoryOpenRequest extends Request {
    @Override
    public @Nullable Response call() {
        assert MinecraftClient.getInstance().player != null;
        assert MinecraftClient.getInstance().interactionManager != null;

        MinecraftClient.getInstance().execute(() -> {
            if (MinecraftClient.getInstance().interactionManager.hasRidingInventory()) {
                MinecraftClient.getInstance().player.openRidingInventory();
            } else {
                if (MinecraftClient.getInstance().currentScreen instanceof InventoryScreen) return;
                MinecraftClient.getInstance().getTutorialManager().onInventoryOpened();
                MinecraftClient.getInstance().setScreen(new InventoryScreen(MinecraftClient.getInstance().player));
            }
        });

        return null;
    }
}
