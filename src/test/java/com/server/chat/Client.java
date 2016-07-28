package com.server.chat;

import java.util.Scanner;

import static org.zeromq.ZMQ.*;

class Client {
    private final static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        try (Context context = context(1)) {
            ConnectionConfig config = new ConnectionConfig(context);

            System.out.print("What is your login? ");
            String login = scanner.nextLine().trim();
            System.out.print("What is your password? ");
            String password = scanner.nextLine().trim();

            AuthorisationController authorizationController = new AuthorisationController(config.getDatabaseRequester());

            if (authorizationController.authorization(login, password)) {
                Socket sender = config.getSender();
                sender.send(login + " has joined");

                Thread send = startSenderThread(login, config);
                Thread receive = startReceiverThread(config);

                send.join();
                receive.join();
            } else {
                System.out.println("Wrong input");
            }
        }
    }


    private static Thread startSenderThread(String login, ConnectionConfig config) {
        Thread send = new Thread(() -> {
            Socket sender = config.getSender();
            while (!Thread.currentThread().isInterrupted()) {
                String messageToSend = scanner.nextLine();
                sender.send(login + ": " + messageToSend);
            }
        });
        send.start();
        return send;
    }

    private static Thread startReceiverThread(ConnectionConfig config) {
        Thread receive = new Thread(() -> {
            Socket receiver = config.getReceiver();
            Poller poller = config.getPoller();
            while (!Thread.currentThread().isInterrupted()) {
                int events = poller.poll();
                if (events > 0) {
                    String message = receiver.recvStr(0);
                    System.out.println(message);
                }
            }
        });
        receive.start();
        return receive;
    }
}