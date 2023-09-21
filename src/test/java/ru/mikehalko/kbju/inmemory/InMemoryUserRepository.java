package ru.mikehalko.kbju.inmemory;

import ru.mikehalko.kbju.model.user.User;
import ru.mikehalko.kbju.repository.UserRepository;
import static ru.mikehalko.kbju.inmemory.InMemoryExceptions.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryUserRepository implements UserRepository {

    private final Map<Integer, User> usersMap = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(100);

    @Override
    public User save(User user) {
        if (user.isNew()) {
            usersMap.putIfAbsent(idCounter.getAndIncrement(), user);
        } else {
            User oldUser = usersMap.remove(user.getId());
            checkNull(oldUser, user.getId());
            usersMap.put(user.getId(), user);
        }
        return user;
    }

    @Override
    public boolean delete(int id) {
        User deleted = usersMap.remove(id);
        checkNull(deleted, id);
        return true;
    }

    @Override
    public User get(int id) {
        User user = usersMap.get(id);
        checkNull(user, id);
        return user;
    }

    @Override
    public List<User> getAll() {
        return usersMap.values().stream().collect(Collectors.toList());
    }

    private void checkNull(User user, int id) {
        if (user == null) throw new NotFoundException("not found user with id=" + id);
    }
}
