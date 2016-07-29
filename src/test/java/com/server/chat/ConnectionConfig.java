package com.server.chat;

import static org.zeromq.ZMQ.*;

class ConnectionConfig {
    private Socket receiver;
    private Socket sender;
    private Socket databaseRequester;
    private Poller poller;

    ConnectionConfig(Context context) {
        init(context);
    }

    private void init(Context context) {
        receiveInit(context);
        sendInit(context);
        pollerInit();
        databaseRequesterInit(context);
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

    private void pollerInit() {
        poller = new Poller(0);
        poller.register(receiver, Poller.POLLIN);
    }

    private void databaseRequesterInit(Context context) {
        databaseRequester = context.socket(REQ);
        databaseRequester.connect("tcp://10.66.160.204:11000");
    }

    Socket getDatabaseRequester() {
        return databaseRequester;
    }

    Socket getReceiver() {
        return receiver;
    }

    Poller getPoller() {
        return poller;
    }

    Socket getSender() {
        return sender;
    }
}
