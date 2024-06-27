package me.teawin.teapilot.protocol;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.teawin.teapilot.RequestDispatcher;
import me.teawin.teapilot.Teapilot;
import me.teawin.teapilot.protocol.response.ErrorResponse;

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
    private final TeapilotServer teapilotServer;

    public ClientHandler(Socket clientSocket, RequestDispatcher manager, TeapilotServer teapilotServer) throws IOException {
        this.clientSocket = clientSocket;
        this.manager = manager;
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.teapilotServer = teapilotServer;
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
        } catch (IOException exception) {
            TeapilotServer.LOGGER.error("Read failed: " + exception.getMessage());
        } finally {
            try {
                clientSocket.close();
                teapilotServer.clients.remove(clientSocket);
                TeapilotServer.LOGGER.info("Client disconnected");
            } catch (IOException e) {
                TeapilotServer.LOGGER.error("Failed to close client socket: " + e.getMessage());
            }
        }
    }

    void readPacket(String message) {
        if (Teapilot.flagsManager.isEnabled("DEBUG_PACKET_LOGGER"))
            TeapilotServer.LOGGER.info("Packet in:\n" + message);

        JsonElement jsonElement = JsonParser.parseString(message);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement replyId = jsonObject.get("replyId");

        String method = jsonObject.get("method").getAsString();

        if (manager.listeners.containsKey(method)) {
            manager.dispatch((Request) GsonConfigurator.gson.fromJson(jsonElement, manager.listeners.get(method))).thenApply(replyJsonObject -> {
                if (replyId != null) {
                    if (replyJsonObject != null) {
                        replyJsonObject.setReplyId(replyId);
                        teapilotServer.broadcast(replyJsonObject);
                    } else {
                        var response = new Response();
                        response.setReplyId(replyId);
                        teapilotServer.broadcast(response);
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

                    teapilotServer.broadcast(errorJson);
                }
                TeapilotServer.LOGGER.error("Error evaluate: " + throwable.getMessage() + '\n' + multiLineTraceString);
                return null;
            });
        } else {
            if (replyId != null) {
                var response = new Response();
                response.setReplyId(replyId);
                teapilotServer.broadcast(response);
            }
        }
    }
}