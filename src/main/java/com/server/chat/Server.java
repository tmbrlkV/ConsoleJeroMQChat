package com.server.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

class Server {
    private static Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws InterruptedException {
        logger.debug("Start chat server");

        try (ZMQ.Context context = ZMQ.context(1)) {
            ZMQ.Socket publisher = context.socket(ZMQ.PUB);
            ZMQ.Socket receiver = context.socket(ZMQ.PULL);
            publisher.bind("tcp://*:10000");
            receiver.bind("tcp://*:10001");

            logger.debug("listening for incoming connection...");
            while (!Thread.currentThread().isInterrupted()) {
                String message = receiver.recvStr();
                logger.debug(message);
                publisher.send(message);
            }
        }
    }
}
