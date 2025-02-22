package me.teawin.teapilot.protocol.request;

import me.teawin.teapilot.mixin.accessor.PlayerListHudAccessor;
import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class TabListRequest extends Request {
    @Override
    public @Nullable Response call() throws Exception {
        PlayerListHudAccessor hud = (PlayerListHudAccessor) MinecraftClient.getInstance().inGameHud.getPlayerListHud();

        Text footer = hud.getFooter();
        Text header = hud.getHeader();

        return new TabListResponse(footer, header);
    }

    public static class TabListResponse extends Response {
        @Nullable Text footer;
        @Nullable Text header;

        TabListResponse(@Nullable Text footer, @Nullable Text header) {
            this.footer = footer;
            this.header = header;
        }
    }
}
