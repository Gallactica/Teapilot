package me.teawin.soulkeeper.mixin;

import com.mojang.datafixers.util.Pair;
import me.teawin.soulkeeper.proposal.ScoreboardStore;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;
import java.util.List;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "renderScoreboardSidebar", at = @At(value = "INVOKE", target = "Ljava/util/List;iterator()Ljava/util/Iterator;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    public void renderScoreboardSidebar(DrawContext context, ScoreboardObjective objective, CallbackInfo ci, Scoreboard scoreboard, Collection collection, List list, List<Pair<ScoreboardPlayerScore, Text>> list2, Text text, int i, int j, int k, int l, int m, int n, int o, int p, int q, int r) {
        ScoreboardStore.sidebarTexts = list2;
        ScoreboardStore.sidebarTitle = text;
    }
}
