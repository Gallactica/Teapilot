package me.teawin.soulkeeper.protocol.request;

import me.teawin.soulkeeper.protocol.Request;
import me.teawin.soulkeeper.protocol.Response;
import me.teawin.soulkeeper.proposal.TooltipExchanger;
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
