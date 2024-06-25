package me.teawin.soulkeeper.protocol;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.teawin.soulkeeper.RemoteProcedureManager;
import me.teawin.soulkeeper.Soulkeeper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final BufferedReader in;
    private final PrintWriter out;
    private final RemoteProcedureManager manager;
    private final TCPServer tcpServer;

    public ClientHandler(Socket clientSocket, RemoteProcedureManager manager, TCPServer tcpServer) throws IOException {
        this.clientSocket = clientSocket;
        this.manager = manager;
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.tcpServer = tcpServer;
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
            tcpServer.clients.remove(clientSocket);

            TCPServer.LOGGER.info("Client disconnected");
        } catch (IOException e) {

            TCPServer.LOGGER.error("Read failed: " + e.getMessage());
            // Remove the client from the list when an exception occurs
            tcpServer.clients.remove(clientSocket);

            TCPServer.LOGGER.info("Client disconnected");
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                TCPServer.LOGGER.error("Failed to close client socket: " + e.getMessage());
            }
        }
    }


    void readPacket(String message) {
        if (Soulkeeper.flagsManager.isEnabled("DEBUG_PACKET_LOGGER"))
            TCPServer.LOGGER.info("Packet in:\n" + message);

        JsonElement jsonElement = JsonParser.parseString(message);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement replyId = jsonObject.get("replyId");

        manager.dispatch(jsonObject.get("method").getAsString(), jsonObject).thenApply(replyJsonObject -> {
            if (replyId != null) {
                if (replyJsonObject != null) {
                    replyJsonObject.add("replyId", replyId);
                    tcpServer.broadcast(replyJsonObject);
                } else {
                    var obj = new JsonObject();
                    obj.add("replyId", replyId);
                    tcpServer.broadcast(obj);
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
                var errorJson = new JsonObject();
                errorJson.add("replyId", replyId);
                errorJson.addProperty("error", throwable.getMessage());
                errorJson.addProperty("stack", multiLineTraceString);

                tcpServer.broadcast(errorJson);
            }
            TCPServer.LOGGER.error("Error evaluate: " + throwable.getMessage() + '\n' + multiLineTraceString);
            return null;
        });
    }
}