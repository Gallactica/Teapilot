package me.teawin.soulkeeper.protocol.request.scoreboard;

import com.mojang.datafixers.util.Pair;
import me.teawin.soulkeeper.protocol.Request;
import me.teawin.soulkeeper.protocol.Response;
import me.teawin.soulkeeper.protocol.response.scoreboard.SidebarResponse;
import me.teawin.soulkeeper.proposal.ScoreboardStore;
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
