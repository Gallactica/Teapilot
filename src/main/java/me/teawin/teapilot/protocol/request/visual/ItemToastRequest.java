package me.teawin.teapilot.protocol.request.visual;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.visual.ItemToast;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class ItemToastRequest extends Request {
    private String id;
    private Text title;
    private Text description;

    @Override
    public @Nullable Response call() throws Exception {

        if (title == null) title = Text.empty();
        if (description == null) description = Text.empty();

        ItemStack item = Registries.ITEM.get(Identifier.tryParse(id))
                .getDefaultStack();

        ItemToast.show(MinecraftClient.getInstance()
                .getToastManager(), title, description, item);

        return null;
    }
}
