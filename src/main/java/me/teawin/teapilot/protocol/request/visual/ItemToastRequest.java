package me.teawin.teapilot.protocol.request.visual;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.visual.ItemToast;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class ItemToastRequest extends Request {
    private String id;
    private Text title;
    private Text description;

    @Override
    public @Nullable Response call() throws Exception {

        if (title == null) title = Text.empty();
        if (description == null) description = Text.empty();

        ItemToast itemToast = new ItemToast(id, title, description);

        MinecraftClient.getInstance().getToastManager().add(itemToast);

        return null;
    }
}
