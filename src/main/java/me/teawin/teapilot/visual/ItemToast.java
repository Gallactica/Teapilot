package me.teawin.teapilot.visual;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;

public class ItemToast implements Toast {
    private static final Identifier TEXTURE = Identifier.ofVanilla("toast/recipe");
    private static final long DEFAULT_DURATION_MS = 5000L;
    private long startTime;
    private boolean justUpdated;
    private Toast.Visibility visibility = Toast.Visibility.HIDE;

    public Text title = Text.empty();
    public Text description = Text.empty();
    public ItemStack displayItem = ItemStack.EMPTY;

    private ItemToast() {
    }

    @Override
    public Toast.Visibility getVisibility() {
        return this.visibility;
    }

    @Override
    public void update(ToastManager manager, long time) {
        if (this.justUpdated) {
            this.startTime = time;
            this.justUpdated = false;
        }

        if (this.displayItem.isEmpty()) {
            this.visibility = Toast.Visibility.HIDE;
        } else {
            this.visibility =
                    (double) (time - this.startTime) >= DEFAULT_DURATION_MS * manager.getNotificationDisplayTimeMultiplier()
                            ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
        }

    }

    @Override
    public void draw(DrawContext context, TextRenderer textRenderer, long startTime) {
        context.drawGuiTexture(RenderLayer::getGuiTextured, TEXTURE, 0, 0, this.getWidth(), this.getHeight());
        context.drawText(textRenderer, title, 30, 7, Colors.PURPLE, false);
        context.drawText(textRenderer, description, 30, 18, Colors.BLACK, false);
        context.getMatrices()
                .push();
        context.getMatrices()
                .scale(1.5F, 1.5F, 1.5F);
        context.drawItemWithoutEntity(displayItem, 2, 2);
        context.getMatrices()
                .pop();
    }

    public static void show(ToastManager toastManager, Text title, Text description, ItemStack item) {
        ItemToast itemToast = toastManager.getToast(ItemToast.class, TYPE);
        if (itemToast == null) {
            itemToast = new ItemToast();
            toastManager.add(itemToast);
        }
        itemToast.justUpdated = true;
        itemToast.title = title;
        itemToast.description = description;
        itemToast.displayItem = item;
    }
}
