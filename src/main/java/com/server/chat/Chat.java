package com.server.chat;

import java.util.*;
import java.util.concurrent.*;

public class Chat implements Runnable {
    private List<SocketHandler> clients = new CopyOnWriteArrayList<>();
    private BlockingQueue<String> messages = new LinkedBlockingQueue<>();

    @Override
    public void run() {
        while (true) {
            try {
                String message = messages.take();
                realBroadcast(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void addClient(SocketHandler handler) {
        clients.add(handler);
    }

    public void removeClient(SocketHandler handler) {
        clients.remove(handler);
    }

    public void broadcast(String message) {
        messages.offer(message);
    }

    private void realBroadcast(String messages) {
        for (SocketHandler socketHandler : clients) {
            socketHandler.send(messages);
        }
    }
}
