package me.teawin.teapilot.protocol.request;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.proposal.TooltipExchanger;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TooltipRequest extends Request {
    List<Text> texts;

    @Override
    public @Nullable Response call() {
        TooltipExchanger.textList = texts;

        return null;
    }
}
