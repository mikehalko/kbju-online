package ru.mikehalko.kbju.repository.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mikehalko.kbju.model.user.UserCredential;
import ru.mikehalko.kbju.repository.UserCredentialRepository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static ru.mikehalko.kbju.util.sql.ConnectDataBase.executeQuery;
import static ru.mikehalko.kbju.util.sql.ConstantProperties.*;

public class UserCredentialRepositorySQL implements UserCredentialRepository, Connectable {

    private final Logger log = LoggerFactory.getLogger(UserCredentialRepositorySQL.class);
    private Connection connection;

    private static UserCredentialRepositorySQL instance;

    private UserCredentialRepositorySQL() {
    }

    public static synchronized UserCredentialRepositorySQL getInstance() {
        if (instance == null) {
            instance = new UserCredentialRepositorySQL();
        }
        return instance;
    }


    @Override
    public boolean save(UserCredential credential, int userId) {
        log.debug("save/update credential={}", credential);
        String credentialSaveSQL = String.format(
                "INSERT INTO \"%1$s\" (%2$s, %3$s, %4$s) " +
                        "VALUES (%5$d, '%6$s', '%7$s') " +
                        "RETURNING %8$s;",
                USER_CREDENTIAL_TABLE,
                USER_CREDENTIAL_USER_ID, USER_CREDENTIAL_LOGIN, USER_CREDENTIAL_PASSWORD,
                userId, credential.getLogin(), credential.getPassword(),
                USER_CREDENTIAL_USER_ID
        );
        log.debug("SQL = {}", hidePassword(credentialSaveSQL, credential.getPassword()));


        boolean access = false;
        try (ResultSet rs = executeQuery(connection, credentialSaveSQL)) {
            rs.next();
            access = rs.getInt(USER_CREDENTIAL_USER_ID) == credential.getUserId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return access;
    }

    @Override
    public boolean update(UserCredential credential) {
        log.debug("update credential = {}", credential);
        if (credential == null || credential.getUserId() <= 0 ||
                credential.getLogin() == null || credential.getPassword() == null)
            throw  new RuntimeException(
                    String.format("credential(%s), login(%s) or password(%s) null or userId(%d) <= 0 ",
                    credential,
                    credential != null ? credential.getLogin() : ".",
                    credential != null ? credential.getPassword() : ".",
                    credential != null ? credential.getUserId() : 999999));


        String credentialUpdateSQL = String.format(
                "UPDATE \"%1$s\" SET " +
                        "%2$s = '%3$s' " +
                        "WHERE %4$s = %5$d AND %6$s = '%7$s' " +
                        "RETURNING %8$s;",
                USER_CREDENTIAL_TABLE, // 1
                USER_CREDENTIAL_PASSWORD, credential.getPassword(),  // 2 3
                USER_CREDENTIAL_USER_ID, credential.getUserId(), USER_CREDENTIAL_LOGIN, credential.getLogin(), // 4 5 6 7
                USER_CREDENTIAL_USER_ID // 8
        );

        log.debug("SQL = {}", hidePassword(credentialUpdateSQL, credential.getPassword()));


        boolean access = false;
        try (ResultSet rs = executeQuery(connection, credentialUpdateSQL)) {
            rs.next();
            access = rs.getInt(USER_CREDENTIAL_USER_ID) == credential.getUserId();
        } catch (SQLException e) {
            log.error("GET INT AFTER UPDATE FAIL"); // TODO свой exc и убрать
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
                USER_CREDENTIAL_LOGIN, credential.getLogin(), USER_CREDENTIAL_PASSWORD, credential.getPassword()
        );


        log.debug("SQL = {}", hidePassword(credentialFindSQL, credential.getPassword()));

        int findId = 0;
        try (ResultSet rs = executeQuery(connection, credentialFindSQL)) {
            if (rs.next()) {
                findId = rs.getInt(USER_CREDENTIAL_USER_ID);
            }
        } catch (SQLException e) {
            log.error("GET INT AFTER SELECT FAIL"); // TODO убрать и сделать exception
            throw new RuntimeException(e);
        }

        return findId;
    }

    @Override
    public boolean setLogin(UserCredential credential) {
        log.debug("find login={}", credential); // TODO убрать..

        if (credential == null || credential.getLogin() != null) {
            throw new RuntimeException(); // TODO свой exception
        }
        if (credential.getUserId() <= 0) {
            throw new RuntimeException(); // TODO свой exception
        }

        String credentialFindSQL = String.format(
                "SELECT %1$s FROM \"%2$s\" WHERE %3$s = '%4$s' AND %5$s = '%6$s';",
                USER_CREDENTIAL_LOGIN, USER_CREDENTIAL_TABLE,
                USER_CREDENTIAL_USER_ID, credential.getUserId(), USER_CREDENTIAL_PASSWORD, credential.getPassword()
        );

        log.debug("SQL = {}", hidePassword(credentialFindSQL, credential.getPassword()));
        String findLogin = null;
        try (ResultSet rs = executeQuery(connection, credentialFindSQL)) {
            if (rs.next()) {
                findLogin = rs.getString(USER_CREDENTIAL_LOGIN);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // TODO свой exception
        }

        log.debug("find login = {}", findLogin); // TODO убрать
        if (findLogin == null || findLogin.isEmpty()) return false;
        credential.setLogin(findLogin);
        return true;
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
