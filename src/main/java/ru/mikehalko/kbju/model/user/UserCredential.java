package ru.mikehalko.kbju.model.user;

import java.util.Objects;

public class UserCredential {
    private User owner;
    private String login;
    private String password;

    public UserCredential(User owner) {
        this.owner = owner;
    }

    public UserCredential(String login, String passwordHash) {
        this.login = login;
        this.password = passwordHash;
    }

    public UserCredential(User owner, String login, String password) {
        this.owner = owner;
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserId() {
        return owner.getId();
    }

    public User getUser() {
        return owner;
    }

    public void setUser(User owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return String.format("CREDENTIAL(%s:***%s)", login, password.substring(password.length()-password.length()%5));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCredential that = (UserCredential) o;
        return Objects.equals(login, that.login) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }
}
