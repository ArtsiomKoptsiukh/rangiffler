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

    @Override
    public UserJson createUser(UserJson user) {

        try (Connection connection = DriverManager.getConnection(CFG.spendJdbcUrl(), CFG.dbUsername(), CFG.dbPassword())) {
            connection.setAutoCommit(false);

            SingleConnectionDataSource scd = new SingleConnectionDataSource(connection, true);
            final JdbcTemplate jdbcTemplate = new JdbcTemplate(scd);
            final UUID userId = UUID.randomUUID();
            try {
                jdbcTemplate.update((conn) -> {
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

                jdbcTemplate.update((conn) -> {
                    PreparedStatement ps = conn.prepareStatement(SQL_INSERT_AUTHORITY_SCRIPT);
                    ps.setString(1, userId.toString());
                    ps.setString(2, "read");
                    return ps;
                });

                jdbcTemplate.update(conn -> {
                    PreparedStatement ps = conn.prepareStatement(SQL_INSERT_AUTHORITY_SCRIPT);
                    ps.setString(1, userId.toString());
                    ps.setString(2, "write");
                    return ps;
                });

                connection.commit();

                return new UserJson(userId.toString(), user.username(), user.password(), user.enabled(), user.accountNonExpired(),
                        user.accountNonLocked(), user.credentialsNonExpired());
            } catch (Exception e) {
                connection.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user with authorities in DB", e);
        }
    }

}
