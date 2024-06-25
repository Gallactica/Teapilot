package me.teawin.soulkeeper.protocol;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.teawin.soulkeeper.RequestDispatcher;
import me.teawin.soulkeeper.Soulkeeper;
import me.teawin.soulkeeper.protocol.response.ErrorResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final BufferedReader in;
    private final PrintWriter out;
    private final RequestDispatcher manager;
    private final SoulkeeperServer soulkeeperServer;

    public ClientHandler(Socket clientSocket, RequestDispatcher manager, SoulkeeperServer soulkeeperServer) throws IOException {
        this.clientSocket = clientSocket;
        this.manager = manager;
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.soulkeeperServer = soulkeeperServer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = in.readLine();
                if (message == null) {
                    break;
                }
                String[] packets = message.split("\\r?\\n");
                for (String packet : packets) {
                    readPacket(packet);
                }
            }
            // Remove the client from the list when the connection is closed
            soulkeeperServer.clients.remove(clientSocket);

            SoulkeeperServer.LOGGER.info("Client disconnected");
        } catch (IOException e) {

            SoulkeeperServer.LOGGER.error("Read failed: " + e.getMessage());
            // Remove the client from the list when an exception occurs
            soulkeeperServer.clients.remove(clientSocket);

            SoulkeeperServer.LOGGER.info("Client disconnected");
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                SoulkeeperServer.LOGGER.error("Failed to close client socket: " + e.getMessage());
            }
        }
    }

    void readPacket(String message) {
        if (Soulkeeper.flagsManager.isEnabled("DEBUG_PACKET_LOGGER")) SoulkeeperServer.LOGGER.info("Packet in:\n" + message);

        JsonElement jsonElement = JsonParser.parseString(message);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement replyId = jsonObject.get("replyId");

        String method = jsonObject.get("method").getAsString();

        if (manager.listeners.containsKey(method)) {
            manager.dispatch((Request) GsonConfigurator.gson.fromJson(jsonElement, manager.listeners.get(method))).thenApply(replyJsonObject -> {
                if (replyId != null) {
                    if (replyJsonObject != null) {
                        replyJsonObject.setReplyId(replyId);
                        soulkeeperServer.broadcast(replyJsonObject);
                    } else {
                        var response = new Response();
                        response.setReplyId(replyId);
                        soulkeeperServer.broadcast(response);
                    }
                }
                return null;
            }).exceptionally(throwable -> {
                StackTraceElement[] stackTraceElements = throwable.getStackTrace();
                StringBuilder sb = new StringBuilder();
                for (StackTraceElement element : stackTraceElements) {
                    sb.append(element.toString()).append("\n");
                }
                String multiLineTraceString = sb.toString();

                if (replyId != null) {
                    var errorJson = new ErrorResponse(throwable.getMessage(), multiLineTraceString);
                    errorJson.setReplyId(replyId);

                    soulkeeperServer.broadcast(errorJson);
                }
                SoulkeeperServer.LOGGER.error("Error evaluate: " + throwable.getMessage() + '\n' + multiLineTraceString);
                return null;
            });
        } else {
            if (replyId != null) {
                var response = new Response();
                response.setReplyId(replyId);
                soulkeeperServer.broadcast(response);
            }
        }
    }
}