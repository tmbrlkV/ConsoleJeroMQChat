package com.server.chat;

import static org.zeromq.ZMQ.*;

public class ConnectionConfig {
    private Socket receiver;
    private Socket sender;
    private Poller poller;

    public ConnectionConfig(Context context) {
        init(context);
    }

    private void init(Context context) {
        receiveInit(context);
        sendInit(context);
        pollerInit(context);
    }

    private void receiveInit(Context context) {
        receiver = context.socket(SUB);
        receiver.connect("tcp://localhost:10000");
        receiver.subscribe("".getBytes());
    }

    private void sendInit(Context context) {
        sender = context.socket(PUSH);
        sender.connect("tcp://localhost:10001");
    }

    private void pollerInit(Context context) {
        poller = new Poller(0);
        poller.register(receiver, Poller.POLLIN);
    }

    public Socket getReceiver() {
        return receiver;
    }

    public Poller getPoller() {
        return poller;
    }

    public Socket getSender() {
        return sender;
    }
}
