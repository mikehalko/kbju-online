package ru.mikehalko.kbju.repository.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mikehalko.kbju.model.user.UserCredential;
import ru.mikehalko.kbju.repository.UserCredentialRepository;

import javax.ws.rs.NotSupportedException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static ru.mikehalko.kbju.util.sql.ConnectDataBase.executeQuery;
import static ru.mikehalko.kbju.util.sql.ConstantProperties.*;

public class UserCredentialRepositorySQL implements UserCredentialRepository, Connectable {

    private final Logger log = LoggerFactory.getLogger(UserCredentialRepositorySQL.class);
    private Connection connection;

    private static UserCredentialRepositorySQL instance;

    private UserCredentialRepositorySQL() {}

    public static synchronized UserCredentialRepositorySQL getInstance() {
        if (instance == null) {
            instance = new UserCredentialRepositorySQL();
        }
        return instance;
    }


    @Override
    public boolean save(UserCredential credential, int userId) {
        log.debug("save credential={}", credential);

        String credentialSaveSQL = String.format(
                "INSERT INTO \"%1$s\" (%2$s, %3$s, %4$s) " +
                        "VALUES (%5$d, '%6$s', '%7$s') " +
                        "RETURNING %8$s;",
                USER_CREDENTIAL_TABLE,
                USER_CREDENTIAL_USER_ID, USER_CREDENTIAL_NAME, USER_CREDENTIAL_PASSWORD,
                userId, credential.getName(), credential.getPassword(),
                USER_CREDENTIAL_USER_ID
        );
        log.debug("SQL = {}", hidePassword(credentialSaveSQL, credential.getPassword()));


        boolean access = false;
        try (ResultSet rs = executeQuery(connection, credentialSaveSQL)) {
            rs.next();
            access = rs.getInt(USER_CREDENTIAL_USER_ID) != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return access;
    }

    @Override
    public boolean delete(UserCredential credential) {
        throw new UnsupportedOperationException("delete credential non supported");
    }

    @Override
    public int find(UserCredential credential) {
        log.debug("find credential={}", credential);

        String credentialFindSQL = String.format(
                "SELECT %1$s FROM \"%2$s\" WHERE %3$s = '%4$s' AND %5$s = '%6$s';",
                USER_CREDENTIAL_USER_ID, USER_CREDENTIAL_TABLE,
                USER_CREDENTIAL_NAME, credential.getName(), USER_CREDENTIAL_PASSWORD, credential.getPassword()
        );


        log.debug("SQL = {}", hidePassword(credentialFindSQL, credential.getPassword()));

        int findId = 0;
        try (ResultSet rs = executeQuery(connection, credentialFindSQL)) {
            if (rs.next()) {
                findId = rs.getInt(USER_CREDENTIAL_USER_ID);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return findId;
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    private static String hidePassword(String sql, String password) {
        return sql.replace(password, "_password_");
    }
}
