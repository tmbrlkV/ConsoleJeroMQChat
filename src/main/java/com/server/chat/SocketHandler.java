package com.server.chat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketHandler implements Runnable {
    private static final int THREADS = Math.max(1, Runtime.getRuntime().availableProcessors() / 2);
    private ExecutorService sender = Executors.newFixedThreadPool(THREADS);

    private Socket socket;
    private PrintWriter printWriter;
    private Chat chat;

    public SocketHandler(Socket socket, Chat chat) {
        try {
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            this.chat = chat;
            this.socket = socket;
            chat.addClient(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String message) {
        sender.execute(() -> printWriter.println(message));
    }

    @Override
    public void run() {
        System.out.println(socket);
        try (Scanner scanner = new Scanner(socket.getInputStream())) {
            while (scanner.hasNextLine()) {
                String message = scanner.nextLine();
                chat.broadcast(message);
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            chat.removeClient(this);
        }
    }
}
