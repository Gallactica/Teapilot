package me.teawin.teapilot.protocol.request.client;

import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.resource.language.I18n;
import org.jetbrains.annotations.Nullable;

public class ClientConnectRequest extends Request {
    private String address;

    @Override
    public @Nullable Response call() throws Exception {
        assert MinecraftClient.getInstance().world == null;

        var serverAddress = ServerAddress.parse(address);

        var serverInfo = new ServerInfo(I18n.translate("selectServer.defaultName"), address,
                ServerInfo.ServerType.OTHER);

        MinecraftClient.getInstance()
                .execute(() -> {
                    ConnectScreen.connect(new MultiplayerScreen(new TitleScreen()), MinecraftClient.getInstance(),
                            serverAddress, serverInfo, true, null);
                });

        return null;
    }
}
