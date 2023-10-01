package ru.mikehalko.kbju.repository;


import ru.mikehalko.kbju.model.user.UserCredential;

public interface UserCredentialRepository {
    boolean save(UserCredential credential, int userId);

    boolean update(UserCredential credential);

    boolean delete(UserCredential credential);

    int find(UserCredential credential);

    boolean setLogin(UserCredential credential);
}
