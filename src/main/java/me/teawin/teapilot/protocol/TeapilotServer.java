package me.teawin.teapilot.protocol;

import com.google.gson.JsonObject;
import me.teawin.teapilot.Teapilot;
import me.teawin.teapilot.RequestDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

public class TeapilotServer {
    private final ServerSocket serverSocket;
    public final List<Socket> clients = new CopyOnWriteArrayList<>();

    public static final Logger LOGGER = LoggerFactory.getLogger("Teapilot/Protocol");

    private final RequestDispatcher manager;

    public final int port;


    public TeapilotServer(RequestDispatcher manager) throws IOException {

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

    public void broadcast(Response jsonObject) {
        broadcast(GsonConfigurator.gson.toJson(jsonObject));
    }

    public void broadcast(JsonObject jsonObject) {
        broadcast(GsonConfigurator.gson.toJson(jsonObject));
    }

    public void broadcast(String message) {
        broadcast(message, clients);
    }

    public void broadcast(String message, List<Socket> clients) {
        if (Teapilot.flagsManager.isEnabled("DEBUG_PACKET_LOGGER"))
            TeapilotServer.LOGGER.info("Packet out:\n" + message);

        CompletableFuture.supplyAsync(() -> {
            for (Socket client : clients) {
                try {
                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                    out.println(message);
                } catch (IOException e) {
                    TeapilotServer.LOGGER.error("Write failed: " + e.getMessage());
                }
            }
            return null;
        });
    }
}
