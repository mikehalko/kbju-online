package ru.mikehalko.kbju.util.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mikehalko.kbju.repository.sql.Connectable;
import ru.mikehalko.kbju.util.sql.exception.ConnectionPropertiesIncorrectException;
import ru.mikehalko.kbju.util.sql.exception.DriverClassNotFoundException;

import java.io.IOException;
import java.sql.*;
import java.util.Arrays;


public class ConnectionDataBase {
    private final static Logger log = LoggerFactory.getLogger(ConnectionDataBase.class);
    private static final int CONNECTING_TIMEOUT = 5;
    private static final boolean RECONNECT = true;
    private static final int RECONNECT_TRIES = 5;

    private Connection connectionHold;
    private final String DB_URL;
    private final String USER;
    private final String PASSWORD;
    private final String DB_CLASS_DRIVER;

    private ConnectionDataBase(String DB_URL, String USER, String PASSWORD, String DB_CLASS_DRIVER) {
        this.DB_URL = DB_URL;
        this.USER = USER;
        this.PASSWORD = PASSWORD;
        this.DB_CLASS_DRIVER = DB_CLASS_DRIVER;
    }

    private Connection connect() throws ClassNotFoundException, SQLException {
        Connection connection = null;

        try {
            log.debug("Load JDBC driver");
            Class.forName(DB_CLASS_DRIVER);
        } catch (ClassNotFoundException e) {
            log.debug("JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            throw e;
        }

        log.debug("JDBC Driver successfully connected");

        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);

        } catch (SQLException e) {
            log.debug("Connection Failed");
            e.printStackTrace();
            throw e;
        }


        if (connection != null) {
            log.debug("Connect to database = {}", connection.isValid(CONNECTING_TIMEOUT));
            log.debug("You successfully connected to database now");
        } else {
            log.debug("Failed to make connection to database");
            throw new RuntimeException("ConnectDataBase Failed to make connection to database");
        }

        return connection;
    }

    public static ConnectionDataBase getConnection(String dbUrl, String user, String password, String classDriver) throws IOException {
        ConnectionDataBase connection = new ConnectionDataBase(dbUrl, user, password, classDriver);
        try {
            connection.connectionHold = connection.connect(); // иначе
        } catch (SQLException e) {
            log.error("wrong user, pass or url db");
            throw new ConnectionPropertiesIncorrectException(e);
        } catch (ClassNotFoundException e) {
            log.error("driver class db not found");
            throw new DriverClassNotFoundException(e);
        }

        return connection;
    }

    public Statement createStatement() throws SQLException {
        return connectionHold.createStatement();
    }

    public ResultSet executeQuery(String sql) {
        ResultSet resultSet = null;
        try {
            Statement statement = connectionHold.createStatement();
            try {
                resultSet = statement.executeQuery(sql);
            } catch (SQLException e) {
                log.error("catch exception 2", e);
                if(!RECONNECT) {
                    log.debug("reconnect false, throw exception");
                    throw new RuntimeException(e);
                }
                else {
                    try {
                        reconnect();
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    return executeQuery(sql);
                }
            }
        } catch (SQLException e) {
            log.error("catch exception 1", e);
            throw new RuntimeException(e);
        }

        return resultSet;
    }

    public void reconnect() throws SQLException, ClassNotFoundException {
        log.debug("reconnect");
        if (connectionHold != null && checkConnection()) connectionHold.close();
        Connection newConnection = null;
        for (int i = 1; i <= RECONNECT_TRIES; i++) {
            log.debug("attempt: {}", i);
            newConnection = connect();
            if (newConnection.isValid(CONNECTING_TIMEOUT)) break;
        }
        if (newConnection == null || !newConnection.isValid(CONNECTING_TIMEOUT)) throw new RuntimeException(); // TODO свой exception
        connectionHold = newConnection;
    }

    public void init(Connectable[] needReconnect) {
        log.debug("init = {}", Arrays.toString(needReconnect));
        for (Connectable hc : needReconnect) {
            hc.setConnection(this);
        }
    }

    public boolean checkConnection() {
        boolean connectionIsValid = true;
        try {
            if (!(connectionIsValid = connectionHold.isValid(5))) {
                log.debug("connection is not valid!");
            } else {
                log.debug("connect is valid");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connectionIsValid;
    }

    public void close() throws SQLException {
        connectionHold.close();
    }
}
