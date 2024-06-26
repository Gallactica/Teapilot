package me.teawin.teapilot.visual;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;

public class ItemToast implements Toast {

    private final ItemStack itemStack;
    private final Text title;
    private final Text description;

    private int titleColor = Colors.WHITE;
    private int descriptionColor = Colors.GRAY;

    private long startTime;
    private boolean justUpdated;
    private static final double time = 5000.0f;

    public ItemToast(String id, Text title, Text description) {
        this.itemStack = Registries.ITEM.get(Identifier.tryParse(id)).getDefaultStack();
        this.title = title;
        this.description = description;

        if (title.getStyle().getColor() != null) titleColor = title.getStyle().getColor().getRgb();
        if (description.getStyle().getColor() != null) descriptionColor = description.getStyle().getColor().getRgb();
    }

    @Override
    public Toast.Visibility draw(DrawContext context, ToastManager manager, long startTime) {
        if (this.justUpdated) {
            this.startTime = startTime;
            this.justUpdated = false;
        }

        context.drawTexture(TEXTURE, 0, 0, 0, 0, this.getWidth(), this.getHeight());
        context.drawText(manager.getClient().textRenderer, title, 30, 7, titleColor, false);
        context.drawText(manager.getClient().textRenderer, description, 30, 18, descriptionColor, false);
        context.getMatrices().push();
        context.getMatrices().scale(1.5F, 1.5F, 1.5F);
        context.drawItemWithoutEntity(itemStack, 3, 3);
        context.getMatrices().pop();
        return (double) (startTime - this.startTime) >= time * manager.getNotificationDisplayTimeMultiplier() ? Visibility.HIDE : Visibility.SHOW;
    }
}
