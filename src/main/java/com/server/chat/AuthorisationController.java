package com.server.chat;

import com.server.entity.User;
import com.server.json.JsonObjectFactory;
import org.zeromq.ZMQ;

class AuthorisationController {
    private final ZMQ.Socket databaseRequester;

    AuthorisationController(ZMQ.Socket databaseRequester) {
        this.databaseRequester = databaseRequester;
    }

    boolean authorization(String login, String password) throws Exception {

        User user = new User(login, password);
        String jsonString = JsonObjectFactory.getJsonString("getUserByLoginPassword", user);
        databaseRequester.send(jsonString);

        String response = databaseRequester.recvStr();
        user = JsonObjectFactory.getObjectFromJson(response, User.class);

        return user.validation();
    }
}
