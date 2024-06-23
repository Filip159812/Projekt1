package myapp;

import java.util.Random;

public class MultiClientRunner {
    private static final int CLIENT_COUNT = 10;

    public static void main(String[] args) {
        for (int i = 0; i < CLIENT_COUNT; i++) {
            int clientId = i + 1;
            new Thread(() -> {
                try {
                    Thread.sleep(new Random().nextInt(3000)); // Random delay before starting each client
                    Client.main(new String[]{String.valueOf(clientId)});
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
