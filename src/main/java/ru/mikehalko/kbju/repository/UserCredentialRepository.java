package ru.mikehalko.kbju.repository;


import ru.mikehalko.kbju.model.user.UserCredential;

public interface UserCredentialRepository {
    boolean save(UserCredential user, int userId);

    boolean delete(UserCredential credential);

    int find(UserCredential credential);
}
