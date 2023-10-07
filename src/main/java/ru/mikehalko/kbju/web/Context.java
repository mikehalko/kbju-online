package ru.mikehalko.kbju.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mikehalko.kbju.repository.sql.Connectable;
import ru.mikehalko.kbju.repository.sql.MealRepositorySQL;
import ru.mikehalko.kbju.repository.sql.UserCredentialRepositorySQL;
import ru.mikehalko.kbju.repository.sql.UserRepositorySQL;
import ru.mikehalko.kbju.util.sql.ConnectDataBase;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import static ru.mikehalko.kbju.util.sql.ConnectDataBase.initConstantPropertiesAndGetConnection;

public class Context implements ServletContextListener {
    private final Logger log = LoggerFactory.getLogger(Context.class);
    private static Connectable[] havingConnection;    private static final String DB_PROPERTIES_PATH = "/properties/db/postgres.properties";// TODO вписать во внешний файл конфигурации war

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.debug("app initialization");
        ServletContext servletContext = sce.getServletContext();
        String realPathDbProperties = servletContext.getRealPath(DB_PROPERTIES_PATH);
        log.debug("real path db.properties = {}", realPathDbProperties);
        Connection connection = initConstantPropertiesAndGetConnection(realPathDbProperties);
        ConnectDataBase.setConnectionHold(connection);
        setHavingConnection(MealRepositorySQL.getInstance(), UserRepositorySQL.getInstance(),UserCredentialRepositorySQL.getInstance());
        connectAll(connection, getHavingConnection());
        log.debug("app initialization finished");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            ConnectDataBase.getConnectionHold().close();
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

    private static void connectAll(Connection connection, Connectable[] array) {
        for (Connectable hc : array) {
            hc.setConnection(connection);
        }
    }
}
