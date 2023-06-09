package com.idis.shared.database;

import java.sql.*;
import com.zaxxer.hikari.*;

public class Database {
    private static HikariDataSource dataSource;

    public static void configure(String url, String user, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);

        dataSource = new HikariDataSource(config);
    }

    public static void close() throws SQLException {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    protected static ResultSet executeQuery(String query) throws SQLException {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            return statement.executeQuery(query);
        }
    }

    protected static void execute(String query) throws SQLException {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(query);
        }
    }

    protected static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Database connection pool is not configured.");
        }

        return dataSource.getConnection();
    }
}
