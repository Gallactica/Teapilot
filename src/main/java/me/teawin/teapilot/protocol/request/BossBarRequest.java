package me.teawin.teapilot.protocol.request;

import me.teawin.teapilot.mixin.accessor.BossBarHudAccessor;
import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ClientBossBar;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BossBarRequest extends Request {
    @Override
    public @Nullable Response call() throws Exception {
        var hud = MinecraftClient.getInstance().inGameHud.getBossBarHud();
        var bars = ((BossBarHudAccessor) hud).getBossBars();
        return new BossBarResponse(bars.values()
                .stream()
                .toList());
    }

    public static class BossBarResponse extends Response {
        final List<ClientBossBar> bars;

        BossBarResponse(List<ClientBossBar> bars) {
            this.bars = bars;
        }
    }
}
