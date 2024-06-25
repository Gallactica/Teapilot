package me.teawin.teapilot.proposal;

import com.mojang.datafixers.util.Pair;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardStore {
    public static List<Pair<ScoreboardPlayerScore, Text>> sidebarTexts = new ArrayList<>();
    public static Text sidebarTitle = Text.empty();
}
