package io.student.rangiffler.service;

import io.student.rangiffler.config.Config;
import io.student.rangiffler.model.UserJson;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.UUID;

public class UsersDbClient implements UsersClient {

    private static final Config CFG = Config.getInstance();
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    final String SQL_INSERT_USER_SCRIPT = """
                INSERT INTO `rangiffler-auth`.`user` (id, username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)
                VALUES (UUID_TO_BIN(?, true), ?, ?, ?, ?, ?, ?)
                """;
    final String SQL_INSERT_AUTHORITY_SCRIPT = """
                INSERT INTO `rangiffler-auth`.`authority` (user_id, authority)
                VALUES (UUID_TO_BIN(?, true), ?)
                """;
    final String SQL_INSERT_API_USER_SCRIPT = """
                INSERT INTO `rangiffler-api`.`user` (id, username, first_name, last_name, avatar, country_id)
                VALUES (UUID_TO_BIN(?, true), ?, NULL, NULL, NULL, (SELECT id FROM `rangiffler-api`.`country` LIMIT 1))
                """;
    final String SQL_DELETE_USER_FROM_AUTH_SCRIPT = """
            DELETE FROM `rangiffler-auth`.`user`
            WHERE id = UUID_TO_BIN(?, true)
            """;
    final String SQL_DELETE_AUTHORITY_SCRIPT = """
                DELETE FROM `rangiffler-auth`.`authority`
                WHERE user_id = UUID_TO_BIN(?, true)
            """;
    final String SQL_DELETE_USER_FROM_API_SCRIPT = """
            DELETE FROM `rangiffler-api`.`user`
            WHERE id = UUID_TO_BIN(?, true)
            """;

    @Override
    public UserJson createUser(UserJson user) {
        try (Connection authConnection = DriverManager.getConnection(CFG.authJdbcUrl(), CFG.dbUsername(), CFG.dbPassword());
             Connection apiConnection = DriverManager.getConnection(CFG.apiJdbcUrl(), CFG.dbUsername(), CFG.dbPassword())) {
            authConnection.setAutoCommit(false);
            apiConnection.setAutoCommit(false);

            JdbcTemplate authJdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(authConnection, true));
            JdbcTemplate apiJdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(apiConnection, true));
            final UUID userId = UUID.randomUUID();
            try {
                authJdbcTemplate.update((conn) -> {
                    PreparedStatement ps = conn.prepareStatement(SQL_INSERT_USER_SCRIPT);
                    ps.setString(1, userId.toString());
                    ps.setString(2, user.username());
                    ps.setString(3, passwordEncoder.encode(user.password()));
                    ps.setBoolean(4, user.enabled());
                    ps.setBoolean(5, user.accountNonExpired());
                    ps.setBoolean(6, user.accountNonLocked());
                    ps.setBoolean(7, user.credentialsNonExpired());

                    return ps;
                });

                authJdbcTemplate.update((conn) -> {
                    PreparedStatement ps = conn.prepareStatement(SQL_INSERT_AUTHORITY_SCRIPT);
                    ps.setString(1, userId.toString());
                    ps.setString(2, "read");
                    return ps;
                });

                authJdbcTemplate.update(conn -> {
                    PreparedStatement ps = conn.prepareStatement(SQL_INSERT_AUTHORITY_SCRIPT);
                    ps.setString(1, userId.toString());
                    ps.setString(2, "write");
                    return ps;
                });

                apiJdbcTemplate.update(conn -> {
                    PreparedStatement ps = conn.prepareStatement(SQL_INSERT_API_USER_SCRIPT);
                    ps.setString(1, userId.toString());
                    ps.setString(2, user.username());
                    return ps;
                });

                authConnection.commit();
                apiConnection.commit();

                return new UserJson(userId.toString(), user.username(), user.password(), user.enabled(), user.accountNonExpired(),
                        user.accountNonLocked(), user.credentialsNonExpired());
            } catch (Exception e) {
                authConnection.rollback();
                apiConnection.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user in auth/api DBs", e);
        }
    }

    @Override
    public void deleteUser(String userId) {
        try (
                Connection authConnection = DriverManager.getConnection(CFG.authJdbcUrl(), CFG.dbUsername(), CFG.dbPassword());
                Connection apiConnection = DriverManager.getConnection(CFG.apiJdbcUrl(), CFG.dbUsername(), CFG.dbPassword())
        ) {
            authConnection.setAutoCommit(false);
            apiConnection.setAutoCommit(false);

            JdbcTemplate authJdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(authConnection, true));
            JdbcTemplate apiJdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(apiConnection, true));

            try {
                authJdbcTemplate.update(conn -> {
                    PreparedStatement ps = conn.prepareStatement(SQL_DELETE_AUTHORITY_SCRIPT);
                    ps.setString(1, userId);

                    return ps;
                });

                authJdbcTemplate.update(conn -> {
                    PreparedStatement ps = conn.prepareStatement(SQL_DELETE_USER_FROM_AUTH_SCRIPT);
                    ps.setString(1, userId);

                    return ps;
                });

                apiJdbcTemplate.update(conn -> {
                    PreparedStatement ps = conn.prepareStatement(SQL_DELETE_USER_FROM_API_SCRIPT);
                    ps.setString(1, userId);

                    return ps;
                });

                authConnection.commit();
                apiConnection.commit();
            } catch (Exception e) {
                authConnection.rollback();
                apiConnection.rollback();

                throw e;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user from auth/api DBs", e);
        }
    }

}
