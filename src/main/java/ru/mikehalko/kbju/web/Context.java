package ru.mikehalko.kbju.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mikehalko.kbju.repository.sql.Connectable;
import ru.mikehalko.kbju.repository.sql.MealRepositorySQL;
import ru.mikehalko.kbju.repository.sql.UserCredentialRepositorySQL;
import ru.mikehalko.kbju.repository.sql.UserRepositorySQL;
import ru.mikehalko.kbju.util.sql.ConnectionDataBase;
import ru.mikehalko.kbju.util.sql.ConstantProperties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class Context implements ServletContextListener {
    private final Logger log = LoggerFactory.getLogger(Context.class);
    private static Connectable[] havingConnection;    private static final String DB_PROPERTIES_PATH = "/properties/db/postgres.properties";// TODO вписать во внешний файл конфигурации war
    private static ConnectionDataBase connection;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.debug("app initialization");
        ServletContext servletContext = sce.getServletContext();
        String realPathDbProperties = servletContext.getRealPath(DB_PROPERTIES_PATH);
        log.debug("real path db.properties = {}", realPathDbProperties);
        try {
            ConstantProperties.initProperties(realPathDbProperties);
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
