package ru.mikehalko.kbju.util.model;

import ru.mikehalko.kbju.model.user.UserCredential;

public class UserCredentialUtil {
    public static UserCredential createCredential(int user_id, String login, String password) {
        return new UserCredential(user_id, login, password);
    }

    public static UserCredential clone(UserCredential credential) {
        UserCredential clone = new UserCredential(credential.getUserId(), credential.getLogin(), credential.getPassword());
        return clone;
    }

    public static UserCredential[] clone(UserCredential... credentials) {
        UserCredential[] cloneCredentials = new UserCredential[credentials.length];
        for (int i = 0; i < cloneCredentials.length; i++) {
            cloneCredentials[i] = clone(credentials[i]);
        }
        return cloneCredentials;
    }
}
