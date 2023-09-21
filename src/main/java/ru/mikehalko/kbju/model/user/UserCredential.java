package ru.mikehalko.kbju.model.user;

import java.util.Objects;

public class UserCredential {
    private int userId;
    private String name;
    private String password;

    public UserCredential(String name, String passwordHash) {
        this.name = name;
        this.password = passwordHash;
    }

    public UserCredential(int userId, String name, String password) {
        this.userId = userId;
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return String.format("CREDENTIAL(%s:***%s)", name, password.substring(password.length()-password.length()%5));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCredential that = (UserCredential) o;
        return Objects.equals(name, that.name) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password);
    }
}
