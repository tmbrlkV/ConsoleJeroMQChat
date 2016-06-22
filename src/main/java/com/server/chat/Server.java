package com.server.chat;

import org.zeromq.ZMQ;

class Server {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("start server");

        try (ZMQ.Context context = ZMQ.context(1)) {
            ZMQ.Socket publisher = context.socket(ZMQ.PUB);
            ZMQ.Socket receiver = context.socket(ZMQ.PULL);
            publisher.bind("tcp://*:10000");
            receiver.bind("tcp://*:10001");

            System.out.println("listening for incoming connection...");
            while (!Thread.currentThread().isInterrupted()) {
                String message = receiver.recvStr();
                System.out.println(message);
                publisher.send(message);
            }
        }
    }
}
