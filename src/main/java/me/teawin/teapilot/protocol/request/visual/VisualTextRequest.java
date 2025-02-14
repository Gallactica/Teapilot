package me.teawin.teapilot.protocol.request.visual;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.visual.VisualText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class VisualTextRequest extends Request {
    private Text text;

    @Override
    public @Nullable Response call() {
        VisualText.text = Objects.requireNonNullElseGet(text, Text::empty);
        return null;
    }
}
