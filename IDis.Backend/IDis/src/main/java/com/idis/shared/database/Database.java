package com.idis.shared.database;

import java.sql.*;

/**
 * The {@code Database} class is a utility class that manages a connection to a database.
 * This class is primarily used for establishing and closing the connection and executing
 * queries against the database.
 * <p>
 * To use this class, first configure the database connection parameters using the
 * {@code configure} method, and then call {@code connect} to establish the connection.
 * Once you have finished using the connection, call {@code close} to release resources.
 * <p>
 * Example usage:
 * <pre>
 * Database.configure("jdbc:postgresql://localhost:5432/mydb", "username", "password");
 * Database.connect();
 * ResultSet resultSet = Database.executeQuery("SELECT * FROM users");
 * Database.close();
 * </pre>
 */
public class Database {
    private static String url;
    private static String user;
    private static String password;
    private static Connection connection;

    /**
     * Configures the database connection parameters.
     *
     * @param url      the database URL
     * @param user     the database username
     * @param password the database password
     */
    public static void configure(String url, String user, String password) {
        Database.url = url;
        Database.user = user;
        Database.password = password;
    }

    /**
     * Establishes a connection to the database using the previously configured parameters.
     *
     * @throws SQLException if a database access error occurs or the connection parameters are incorrect
     */
    public static void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, user, password);
        }
    }

    /**
     * Closes the database connection and releases any associated resources.
     *
     * @throws SQLException if a database access error occurs
     */
    public static void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * Executes a SQL query against the database and returns the result set.
     *
     * @param query the SQL query to execute
     * @return a {@code ResultSet} containing the results of the query
     * @throws SQLException if a database access error occurs or the connection is not established
     */
    protected static ResultSet executeQuery(String query) throws SQLException {
        if (connection == null) {
            throw new SQLException("Database connection is not established.");
        }

        var statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    /**
     * Executes a given SQL query using a new {@link Connection} and {@link Statement} instances.
     * <p>
     * This method acquires a new database connection, creates a new statement, and
     * executes the provided SQL query. The resources are automatically closed using
     * try-with-resources to ensure proper resource management.
     *
     * @param query The SQL query to be executed.
     * @throws SQLException If an error occurs during the query execution.
     */
    protected static void execute(String query) throws SQLException {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(query);
        }
    }

    /**
     * Returns the active database connection.
     *
     * @return the active {@code Connection} object
     */
    protected static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
