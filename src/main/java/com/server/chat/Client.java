package com.server.chat;

import java.util.Scanner;

import static org.zeromq.ZMQ.*;

public class Client {
    private final static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException {
        try (Context context = context(1)) {
            ConnectionConfig config = new ConnectionConfig(context);

            System.out.print("What is your name? ");
            String name = scanner.nextLine().trim();


            Thread send = new Thread(() -> {
                Socket sender = config.getSender();
                sender.send(name + " has joined");
                while (!Thread.currentThread().isInterrupted()) {
                    String messageToSend = scanner.nextLine();
                    sender.send(name + ": " + messageToSend);
                }
            });
            send.start();

            Thread receive = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    int events = config.getPoller().poll();
                    if (events > 0) {
                        String message = config.getReceiver().recvStr(0);
                        System.out.println(message);
                    }
                }
            });
            receive.start();

            send.join();
            receive.join();
        }
    }
}
