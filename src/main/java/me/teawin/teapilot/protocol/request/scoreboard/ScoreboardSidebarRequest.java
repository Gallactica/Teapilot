package me.teawin.teapilot.protocol.request.scoreboard;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.protocol.response.scoreboard.SidebarResponse;
import me.teawin.teapilot.proposal.ScoreboardStore;
import net.minecraft.client.MinecraftClient;
import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardSidebarRequest extends Request {
    @Override
    public @Nullable Response call() {
        assert MinecraftClient.getInstance().player != null;

        List<Text> texts = new ArrayList<>();

        for (ScoreboardEntry entry : ScoreboardStore.scoreboardEntries) {
            texts.add(entry.name());
            texts.add(Text.of(String.valueOf(entry.value())));
        }

        return new SidebarResponse(ScoreboardStore.sidebarTitle, texts);
    }
}
