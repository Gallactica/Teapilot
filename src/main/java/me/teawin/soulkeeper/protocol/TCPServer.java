package me.teawin.soulkeeper.protocol;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.teawin.soulkeeper.Soulkeeper;
import me.teawin.soulkeeper.RemoteProcedureManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

public class TCPServer {
    private final ServerSocket serverSocket;
    public final List<Socket> clients = new CopyOnWriteArrayList<>();

    public static final Logger LOGGER = LoggerFactory.getLogger("Soulkeeper/Protocol");

    private final RemoteProcedureManager manager;

    public final int port;

    public TCPServer(RemoteProcedureManager manager) throws IOException {

        int port = 9090;
        ServerSocket findServerSocket;
        while (true) {
            try {
                findServerSocket = new ServerSocket(port);
                break;
            } catch (BindException e) {
                port++;
                if (port > 65535) {
                    port = 1024;
                }
            }
        }

        serverSocket = findServerSocket;

        LOGGER.info("Server is running on port " + port);

        this.manager = manager;
        this.port = port;

        LOGGER.info("Server is running and waiting for client connections...");
    }

    Thread currentThread;

    public void start() {
        currentThread = new Thread(() -> {
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    clients.add(clientSocket);
                    LOGGER.info("Client connected!");
                    new Thread(new ClientHandler(clientSocket, manager, this)).start();
                } catch (IOException e) {
                    LOGGER.error("Accept failed: " + e.getMessage());
                }
            }
        });
        currentThread.start();
    }

    public boolean hasClients() {
        return !this.clients.isEmpty();
    }

    private final Gson gson = new Gson();

    public void broadcast(JsonObject jsonObject) {
        broadcast(gson.toJson(jsonObject));
    }

    public void broadcast(String message) {
        broadcast(message, clients);
    }

    public void broadcast(String message, List<Socket> clients) {
        if (Soulkeeper.flagsManager.isEnabled("DEBUG_PACKET_LOGGER"))
            TCPServer.LOGGER.info("Packet out:\n" + message);

        CompletableFuture.supplyAsync(() -> {
            for (Socket client : clients) {
                try {
                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                    out.println(message);
                } catch (IOException e) {
                    TCPServer.LOGGER.error("Write failed: " + e.getMessage());
                }
            }
            return null;
        });
    }
}
