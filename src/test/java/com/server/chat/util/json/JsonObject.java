package com.server.chat.util.json;


import com.server.chat.util.entity.User;

class JsonObject {
    private String command;
    private User user;

    public JsonObject() {}

    JsonObject(String command, User user) {
        this.command = command;
        this.user = user;
    }

    public String getCommand() {
        return command;
    }

    public User getUser() {
        return user;
    }
}
