package me.teawin.teapilot.protocol.request.scoreboard;

import com.mojang.datafixers.util.Pair;
import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.protocol.response.scoreboard.SidebarResponse;
import me.teawin.teapilot.proposal.ScoreboardStore;
import net.minecraft.client.MinecraftClient;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SidebarRequest extends Request {
    @Override
    public @Nullable Response call() {
        assert MinecraftClient.getInstance().player != null;

        List<Text> texts = new ArrayList<>();

        for (Pair<ScoreboardPlayerScore, Text> scoreboardPlayerScoreTextPair : ScoreboardStore.sidebarTexts) {
            texts.add(scoreboardPlayerScoreTextPair.getSecond());
        }

        return new SidebarResponse(ScoreboardStore.sidebarTitle, texts);
    }
}
