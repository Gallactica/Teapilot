package me.teawin.soulkeeper.mixin;

import me.teawin.soulkeeper.Soulkeeper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.teawin.soulkeeper.Soulkeeper.tcpServer;

@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin {

    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo info) {
        Soulkeeper.LOGGER.info("This line is printed by an example mod mixin!");
        ItemStack diamond = new ItemStack(Items.AMETHYST_SHARD);

        MinecraftClient.getInstance().getToastManager().add(
                new Toast() {

                    @Override
                    public Toast.Visibility draw(DrawContext context, ToastManager manager, long startTime) {
                        if (this.justUpdated) {
                            this.startTime = startTime;
                            this.justUpdated = false;
                        }

                        context.drawTexture(TEXTURE, 0, 0, 0, 32, this.getWidth(), this.getHeight());
                        context.drawText(manager.getClient().textRenderer, "Soulkeeper", 30, 7, -11534256, false);
                        context.drawText(manager.getClient().textRenderer, "Порт " + tcpServer.port, 30, 18, -16777216, false);
                        context.drawItemWithoutEntity(diamond, 8, 8);
                        return (double) (startTime - this.startTime) >= 5000.0 * manager.getNotificationDisplayTimeMultiplier() ? Visibility.HIDE : Visibility.SHOW;
                    }

                    private long startTime;
                    private boolean justUpdated;
                }
        );
    }
}
