package ru.mikehalko.kbju.service;

import ru.mikehalko.kbju.model.user.User;
import ru.mikehalko.kbju.repository.UserRepository;

import java.util.List;

public class UserService {

    private final UserRepository repository;
    private static UserService instance;

    private UserService(UserRepository repository) {
        this.repository = repository;
    }

    public static synchronized UserService getInstance(UserRepository repository) {
        if (instance == null) {
            instance = new UserService(repository);
        }
        return instance;
    }

    public User create(User user) {
        return repository.save(user);
    }

    public void delete(int id) {
        repository.delete(id);
    }

    public User get(int id) {
        return repository.get(id);
    }

    public List<User> getAll() {
        return repository.getAll();
    }

    public void update(User user) {
        repository.save(user);
    }

}