package me.teawin.soulkeeper.mixin;

import com.google.gson.JsonObject;
import me.teawin.soulkeeper.JsonUtils;
import me.teawin.soulkeeper.Soulkeeper;
import me.teawin.soulkeeper.SoulkeeperEvents;
import me.teawin.soulkeeper.proposal.TooltipExchanger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {

    @Inject(method = "drawMouseoverTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;Ljava/util/Optional;II)V"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void inject(DrawContext context, int x, int y, CallbackInfo ci, ItemStack itemStack) {
        if (Soulkeeper.flagsManager.isDisabled("INTERCEPT_ITEM_TOOLTIP")) return;

        if (!itemStack.equals(TooltipExchanger.itemStack)) {
            List<Text> textList = itemStack.getTooltip(MinecraftClient.getInstance().player, MinecraftClient.getInstance().options.advancedItemTooltips ? TooltipContext.Default.ADVANCED : TooltipContext.Default.BASIC);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("event", SoulkeeperEvents.TOOLTIP.toString());
            jsonObject.add("item", JsonUtils.fromItemStack(itemStack));
            jsonObject.add("texts", JsonUtils.fromTextList(textList));
            Soulkeeper.soulkeeperServer.broadcast(jsonObject);

//          TooltipExchanger.textList = Collections.emptyList();
            TooltipExchanger.textList = textList;
        }

        TooltipExchanger.itemStack = itemStack;
        TextRenderer textRenderer = ((ScreenAccessor) this).getTextRenderer();

        context.drawTooltip(textRenderer, TooltipExchanger.textList, TooltipExchanger.itemStack.getTooltipData(), x, y);
        ci.cancel();
    }
}
