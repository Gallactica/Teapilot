package me.teawin.teapilot.mixin;

import com.google.gson.JsonObject;
import me.teawin.teapilot.JsonUtils;
import me.teawin.teapilot.Teapilot;
import me.teawin.teapilot.TeapilotEvents;
import me.teawin.teapilot.mixin.accessor.ScreenAccessor;
import me.teawin.teapilot.proposal.TooltipExchanger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {

    @Shadow
    @Final
    protected Text playerInventoryTitle;

    @Inject(method = "drawMouseoverTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;Ljava/util/Optional;IILnet/minecraft/util/Identifier;)V"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void inject(DrawContext drawContext, int x, int y, CallbackInfo ci, ItemStack itemStack) {
        if (Teapilot.flags.isDisabled("INTERCEPT_ITEM_TOOLTIP")) return;

        if (!itemStack.equals(TooltipExchanger.itemStack)) {
            List<Text> textList = Screen.getTooltipFromItem(MinecraftClient.getInstance(), itemStack);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("event", TeapilotEvents.TOOLTIP.toString());
            jsonObject.add("item", JsonUtils.fromItemStack(itemStack));
            jsonObject.add("texts", JsonUtils.fromTextList(textList));
            Teapilot.teapilotServer.broadcast(jsonObject);

            TooltipExchanger.textList = textList;
        }

        TooltipExchanger.itemStack = itemStack;
        TextRenderer textRenderer = ((ScreenAccessor) this).getTextRenderer();

        drawContext.drawTooltip(textRenderer, TooltipExchanger.textList, TooltipExchanger.itemStack.getTooltipData(), x,
                y);
        ci.cancel();
    }
}
