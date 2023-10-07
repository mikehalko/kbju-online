package ru.mikehalko.kbju.repository.sql;

import ru.mikehalko.kbju.util.sql.ConnectionDataBase;

public interface Connectable {
    ConnectionDataBase getConnection();

    void setConnection(ConnectionDataBase connection);
}