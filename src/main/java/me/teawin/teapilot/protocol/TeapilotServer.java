package me.teawin.teapilot.protocol;

import com.google.gson.JsonObject;
import me.teawin.teapilot.TeapilotDispatcher;
import me.teawin.teapilot.Teapilot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TeapilotServer {
    private final ServerSocket serverSocket;
    public final List<Socket> clients = new CopyOnWriteArrayList<>();
    public static final Logger LOGGER = LoggerFactory.getLogger("Teapilot/Protocol");
    private final TeapilotDispatcher manager;
    public final int port;
    public BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    public AtomicInteger connections = new AtomicInteger(0);

    public TeapilotServer(TeapilotDispatcher manager) throws IOException {

        int port = 9090;
        ServerSocket findServerSocket;
        while (true) {
            try {
                findServerSocket = new ServerSocket(port, 0, InetAddress.getByName("0.0.0.0"));
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

    Thread readThread;
    Thread writeThread;

    public void start() {
        readThread = new Thread(() -> {
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    clients.add(clientSocket);
                    LOGGER.info("Client connected!");
                    connections.incrementAndGet();
                    Thread connectionThread = new Thread(new ClientHandler(clientSocket, manager, this));
                    connectionThread.setName("TeapilotConnection");
                    connectionThread.start();
                } catch (IOException e) {
                    LOGGER.error("Accept failed: " + e.getMessage());
                }
            }
        });
        readThread.setName("TeapilotServerReader");
        readThread.start();

        writeThread = new Thread(() -> {
            while (true) {
                String message = null;
                try {
                    message = queue.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (Teapilot.flags.isEnabled("DEBUG_PACKET_LOGGER")) {
                    TeapilotServer.LOGGER.info("Packet out:\n" + message);
                }

                for (Socket client : clients) {
                    try {
                        PrintWriter out = new PrintWriter(client.getOutputStream());
                        out.write(message + '\n');
                        out.flush();
                    } catch (IOException e) {
                        TeapilotServer.LOGGER.error("Write failed: " + e.getMessage());
                    }
                }
            }
        });
        writeThread.setName("TeapilotServerWriter");
        writeThread.start();
    }

    public boolean hasClients() {
        return this.connections.get() != 0;
    }

    public void broadcast(Response jsonObject) {
        if (!hasClients()) {
            return;
        }
        broadcast(GsonConfigurator.gson.toJson(jsonObject));
    }

    public void broadcast(JsonObject jsonObject) {
        if (!hasClients()) {
            return;
        }
        broadcast(GsonConfigurator.gson.toJson(jsonObject));
    }

    public void broadcast(String message) {
        if (!hasClients()) {
            return;
        }
        queue.add(message);
    }
}
