package com.server.util.json;


import com.server.util.entity.User;

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
