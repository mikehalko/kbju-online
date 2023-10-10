package ru.mikehalko.kbju.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mikehalko.kbju.repository.sql.Connectable;
import ru.mikehalko.kbju.repository.sql.MealRepositorySQL;
import ru.mikehalko.kbju.repository.sql.UserCredentialRepositorySQL;
import ru.mikehalko.kbju.repository.sql.UserRepositorySQL;
import ru.mikehalko.kbju.util.sql.ConnectionDataBase;
import ru.mikehalko.kbju.util.sql.ConstantProperties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class Context implements ServletContextListener {
    private final Logger log = LoggerFactory.getLogger(Context.class);
    private static Connectable[] havingConnection;
    private static ConnectionDataBase connection;

    private static final String DB_PROPERTIES_PATH = System.getenv("KBJU_PROPERTIES");

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.debug("app initialization");
        log.debug("servlet context path = {}", sce.getServletContext().getContextPath());
        log.debug("path db.properties = {}", DB_PROPERTIES_PATH);
        try {
            ConstantProperties.initProperties(DB_PROPERTIES_PATH);
            connection = ConnectionDataBase.getConnection(
                    ConstantProperties.DB_URL, ConstantProperties.DB_USER,
                    ConstantProperties.DB_PASS, ConstantProperties.DB_CLASS_DRIVER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setHavingConnection(MealRepositorySQL.getInstance(), UserRepositorySQL.getInstance(),UserCredentialRepositorySQL.getInstance());
        connection.init(getHavingConnection());

        log.debug("app initialization finished");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            connection.close();
            log.debug("held connection closed");
        } catch (SQLException e) {
            log.error("connection data base close error!", e);
            log.error("connection data base close error!", e);
            log.error("connection data base close error!", e);
        }
    }

    // put all object, that need connection
    public static void setHavingConnection(Connectable... havingConnection) {
        Context.havingConnection = havingConnection;
    }

    public static Connectable[] getHavingConnection() {
        if (Objects.isNull(havingConnection)) return new Connectable[0];
        return havingConnection;
    }
}
