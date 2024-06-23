package myapp;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 12345;
    private static final int MAX_CLIENTS = 5;
    private static final Map<String, List<Serializable>> data = new HashMap<>();
    private static final Set<Integer> activeClients = Collections.synchronizedSet(new HashSet<>());

    static {
        data.put("Kot", Arrays.asList(new Kot("Kot_1"), new Kot("Kot_2"), new Kot("Kot_3"), new Kot("Kot_4")));
        data.put("Pies", Arrays.asList(new Pies("Pies_1"), new Pies("Pies_2"), new Pies("Pies_3"), new Pies("Pies_4")));
        data.put("Ptak", Arrays.asList(new Ptak("Ptak_1"), new Ptak("Ptak_2"), new Ptak("Ptak_3"), new Ptak("Ptak_4")));
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(MAX_CLIENTS);
        System.out.println("Server is starting...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                executor.submit(() -> handleClient(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

            int clientId = in.readInt();
            System.out.println("Client connected: " + clientId);

            synchronized (activeClients) {
                if (activeClients.size() >= MAX_CLIENTS) {
                    out.writeObject("REFUSED");
                    System.out.println("Client refused: " + clientId);
                    return;
                } else {
                    activeClients.add(clientId);
                    out.writeObject("OK");
                    System.out.println("Client accepted: " + clientId);
                }
            }

            for (int i = 0; i < 3; i++) {
                System.out.println("Waiting for request from client " + clientId);
                String request = (String) in.readObject();
                System.out.println("Received request from client " + clientId + ": " + request);
                String className = request.split("_")[1];
                List<Serializable> objects = data.get(className);
                if (objects != null) {
                    out.writeObject(new ArrayList<>(objects));
                    System.out.println("Sent " + objects + " to client " + clientId);
                } else {
                    out.writeObject(new Ptak("Default_Ptak"));
                    System.out.println("Sent default object to client " + clientId);
                }
                Thread.sleep(new Random().nextInt(2000));  // Simulate random delay
            }

            activeClients.remove(clientId);
            System.out.println("Client disconnected: " + clientId);

        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
