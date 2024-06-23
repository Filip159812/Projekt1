package myapp;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Client <client_id>");
            return;
        }

        int clientId = Integer.parseInt(args[0]);

        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            System.out.println("Client " + clientId + " connecting to server...");
            out.writeInt(clientId);
            out.flush();

            String status = (String) in.readObject();
            System.out.println("Client " + clientId + " connection status: " + status);

            if ("REFUSED".equals(status)) {
                return;
            }

            List<String> requests = Arrays.asList("get_Kot", "get_Pies", "get_Ptak");
            Collections.shuffle(requests); // Shuffle requests to simulate different client queries

            for (String request : requests) {
                System.out.println("Client " + clientId + " sending request: " + request);
                out.writeObject(request);
                out.flush();
                Object response = in.readObject();
                try {
                    List<?> objects = (List<?>) response;
                    System.out.println("Client " + clientId + " received: " + objects);
                } catch (ClassCastException e) {
                    System.out.println("Client " + clientId + " could not cast response: " + response);
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
