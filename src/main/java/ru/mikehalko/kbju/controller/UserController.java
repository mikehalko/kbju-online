package ru.mikehalko.kbju.controller;


import ru.mikehalko.kbju.model.user.User;
import ru.mikehalko.kbju.service.UserService;

import java.util.List;

public class UserController {

    private final UserService service;
    private static UserController instance;

    private UserController(UserService service) {
        this.service = service;
    }

    public static synchronized UserController getInstance(UserService service) {
        if (instance == null) {
            instance = new UserController(service);
        }
        return instance;
    }

    public List<User> getAll() {
        return service.getAll();
    }

    public User get(int id) {
        return service.get(id);
    }

    public User create(User user) {
        // check new
        return service.create(user);
    }

    public void delete(int id) {
        service.delete(id);
    }

    public void update(User user, int id) {
        service.update(user);
    }
}