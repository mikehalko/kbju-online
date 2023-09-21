package ru.mikehalko.kbju.repository.sql;

import java.sql.Connection;

public interface Connectable {
    Connection getConnection();

    void setConnection(Connection connection);
}