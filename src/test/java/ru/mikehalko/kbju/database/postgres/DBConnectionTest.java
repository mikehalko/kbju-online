package ru.mikehalko.kbju.database.postgres;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DBConnectionTest {
    private final Logger log = LoggerFactory.getLogger(DBConnectionTest.class);

    private static final String PROPERTIES_FILE_PATH = "src/main/resources/postgres.properties";
    private final String DB_URL;
    private final String USER;
    private final String PASS;
    private final Connection connection;

    public DBConnectionTest() {
        try {
            FileInputStream stream = new FileInputStream(PROPERTIES_FILE_PATH);
            Properties properties = new Properties();
            properties.load(stream);
            DB_URL = properties.getProperty("DB.DB_URL");
            USER = properties.getProperty("DB.USER");
            PASS = properties.getProperty("DB.PASS");
        } catch (IOException e) {
            log.error("property file read error", e);
            throw new RuntimeException(e);
        }

        try {
            connection = connectDataBase();
        } catch (SQLException | ClassNotFoundException e) {
            log.error("connect to database error", e);
            throw new RuntimeException(e);
        }
    }

    private Connection connectDataBase() throws SQLException, ClassNotFoundException {
        log.debug("Testing connection to PostgreSQL JDBC");
        Connection connection = null;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            log.debug("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            throw e;
        }

        log.debug("PostgreSQL JDBC Driver successfully connected");

        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

        } catch (SQLException e) {
            log.debug("Connection Failed");
            e.printStackTrace();
            throw e;
        }

        if (connection != null) {
            log.debug("You successfully connected to database now");
        } else {
            log.debug("Failed to make connection to database");
        }

        return connection;
    }
}
