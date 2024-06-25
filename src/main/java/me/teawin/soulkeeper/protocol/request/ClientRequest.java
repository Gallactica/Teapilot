package me.teawin.soulkeeper.protocol.request;

import me.teawin.soulkeeper.protocol.Request;
import me.teawin.soulkeeper.protocol.response.ClientResponse;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ClientRequest extends Request {
    private static CompletableFuture<ClientResponse> getWindowStat() {

        CompletableFuture<ClientResponse> future = new CompletableFuture<>();

        MinecraftClient.getInstance().execute(() -> {
            Window window = MinecraftClient.getInstance().getWindow();

            ClientResponse response = new ClientResponse();
            response.setWidth(window.getWidth());
            response.setHeight(window.getHeight());
            response.setX(window.getX());
            response.setY(window.getY());
            response.setScale(window.getScaleFactor());
            response.setFov(MinecraftClient.getInstance().options.getFov().getValue());
            response.setFullscreen(window.isFullscreen());

            future.complete(response);
        });

        return future;
    }

    @Override
    public ClientResponse call() {
        CompletableFuture<ClientResponse> stat = getWindowStat();
        try {
            return stat.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
