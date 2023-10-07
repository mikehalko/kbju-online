package ru.mikehalko.kbju.util.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mikehalko.kbju.repository.sql.Connectable;
import ru.mikehalko.kbju.util.sql.exception.ConnectionPropertiesIncorrectException;
import ru.mikehalko.kbju.util.sql.exception.DriverClassNotFoundException;
import ru.mikehalko.kbju.util.sql.exception.PropertiesFileNotFoundException;

import java.io.IOException;
import java.sql.*;

import static ru.mikehalko.kbju.util.sql.ConstantProperties.*;

public class ConnectDataBase {
    private static final Logger log = LoggerFactory.getLogger(ConnectDataBase.class);

    private static Connection connectionHold;
    private static final int CONNECTING_TIMEOUT = 5;

    private static Connection connect() throws ClassNotFoundException, SQLException {
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
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

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

    private static Connection reconnect(Connectable[] needReconnect) throws SQLException, ClassNotFoundException {
        Connection connection = connect();
        for (Connectable hc : needReconnect) {
            hc.setConnection(connection);
        }
        return connection;
    }

    public static Connection initConstantPropertiesAndGetConnection(String realPathDbProperties) {
        Connection connection = null;
        try {
            connection = ConnectDataBase.readPropertiesAndConnection(realPathDbProperties);
        } catch (IOException e) {
            log.error("wrong path db.properties or file not exist");
            throw new PropertiesFileNotFoundException(e);
        } catch (SQLException e) {
            log.error("wrong user, pass or url db");
            throw new ConnectionPropertiesIncorrectException(e);
        } catch (ClassNotFoundException e) {
            log.error("driver class db not found");
            throw new DriverClassNotFoundException(e);
        }
        return connection;
    }

    private static Connection readPropertiesAndConnection(String properties_path) throws SQLException, ClassNotFoundException, IOException {
        readAndInitProperties(properties_path);
        return connect();
    }

    private static void readAndInitProperties(String properties_path) throws IOException {
        try {
            ConstantProperties.initDBAll(properties_path);
            log.debug("Properties init from = {}", properties_path);
        } catch (IOException e) {
            log.error("Reading property file error", e);
            throw e;
        }
    }

    public static ResultSet executeQuery(Connection connection, String sql) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    } // TODO reconnect

    //private static boolean reconnect = true;
//    public static ResultSet executeQuery(String sql) {
//        ResultSet resultSet = null;
//        try {
//            Statement statement = connectionHold.createStatement();
//            try {
//                resultSet = statement.executeQuery(sql);
//            } catch (SQLException e) {
//                log.error("catch exception 2", e);
//                if(!reconnect) {
//                    log.debug("reconnect false, throw exception");
//                    throw new RuntimeException(e);
//                }
//                else {
//                    reconnect = false;
//                    try {
//                        setConnectionHold(connect());
//                        return executeQuery(sql);
//                    } catch (ClassNotFoundException ex) {
//                        throw new RuntimeException(ex);
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            log.error("catch exception 1", e);
//            throw new RuntimeException(e);
//        }
//
//        return resultSet;
//    }

    public static Connection reconnectIfNeed(Connection connect, Connectable... needReconnect) throws SQLException, ClassNotFoundException {
        return !checkConnection(connect) ? reconnect(needReconnect) : ConnectDataBase.connectionHold;
    }

    public static boolean checkConnection(Connection connect) {
        boolean connectionIsValid = true;
        try {
            if (!(connectionIsValid = connect.isValid(5))) {
                log.error("WARNING! CONNECTION NOT VALID!");
                log.error("WARNING! CONNECTION NOT VALID!");
                log.error("WARNING! CONNECTION NOT VALID!");
            }
            log.debug("base connect is valid = {}", connectionIsValid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connectionIsValid;
    }

    public static void setConnectionHold(Connection connectionHold) {
        ConnectDataBase.connectionHold = connectionHold;
    }

    public static Connection getConnectionHold() {
        return connectionHold;
    }
}
