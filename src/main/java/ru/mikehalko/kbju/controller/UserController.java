package ru.mikehalko.kbju.controller;


import ru.mikehalko.kbju.model.User;
import ru.mikehalko.kbju.service.UserService;

import java.util.List;

public class UserController {

    private UserService service;

    public UserController(UserService service) {
        this.service = service;
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