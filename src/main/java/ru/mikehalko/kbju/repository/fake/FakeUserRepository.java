package ru.mikehalko.kbju.repository.fake;

import ru.mikehalko.kbju.model.User;
import ru.mikehalko.kbju.repository.UserRepository;
import ru.mikehalko.kbju.util.SecurityUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class FakeUserRepository implements UserRepository {
    private static List<User> users = new LinkedList<>();
    private static User USER = SecurityUtil.getUser();

    public User save(User user) {
        users.add(user);
        return user;
    }

    public boolean delete(int id) {
        return users.removeIf(user -> user.getId() == id);
        // и удалить бы связанные meals
    }

    public User get(int id) {
        Optional<User> optionalUser = users.stream().filter(meal -> meal.getId() == id).findAny();
        if (optionalUser.isPresent())
            return optionalUser.get();
        else
            throw new RuntimeException();
    }

    public List<User> getAll() {
        return users;
    }
}
