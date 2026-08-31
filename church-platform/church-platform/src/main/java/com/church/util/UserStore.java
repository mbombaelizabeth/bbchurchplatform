package com.church.util;

import com.church.model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class UserStore {

    private static final UserStore INSTANCE = new UserStore();

    private final Map<String, User> users = new ConcurrentHashMap<>();

    private UserStore() {
    }

    public static UserStore getInstance() {
        return INSTANCE;
    }

    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public boolean validate(String username, String password) {
        User user = users.get(username);
        return user != null && user.getPassword().equals(password);
    }
}
