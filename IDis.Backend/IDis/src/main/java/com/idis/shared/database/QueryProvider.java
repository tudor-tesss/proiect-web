package com.idis.shared.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idis.shared.core.AggregateRoot;
import com.idis.shared.serialization.Serialization;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.idis.shared.functional.FunctionalExtensions.any;

public class QueryProvider extends Database {
    private static List<Class<? extends AggregateRoot>> managedClasses = new ArrayList<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Executor DB_EXECUTOR = Executors.newFixedThreadPool(32);

    public static void initiate(String url, String user, String password) throws SQLException {
        try {
            configure(url, user, password);
            createTables();
        } catch (Exception e) {
            throw new SQLException("Failed to connect to database.", e);
        }
    }

    public static <T extends AggregateRoot> CompletableFuture<List<T>> getAllAsync(Class<T> clazz) {
        return CompletableFuture.supplyAsync(() -> getAll(clazz), DB_EXECUTOR);
    }

    public static <T extends AggregateRoot> CompletableFuture<Optional<T>> getByIdAsync(Class<T> clazz, UUID id) {
        return CompletableFuture.supplyAsync(() -> getById(clazz, id), DB_EXECUTOR);
    }

    public static <T extends AggregateRoot> List<T> getAll(Class<T> clazz) {
        String tableName = clazz.getSimpleName() + "_table";
        String query = "SELECT * FROM " + tableName;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            List<T> results = new ArrayList<>();

            while (resultSet.next()) {
                UUID id = resultSet.getObject("id", UUID.class);
                String jsonValue = resultSet.getString("value");

                ObjectMapper objectMapper = new ObjectMapper();
                T instance = objectMapper.readValue(jsonValue, clazz);

                results.add(instance);
            }

            return results;
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Error retrieving data from the database.", e);
        }
    }

    public static <T extends AggregateRoot> Optional<T> getById(Class<T> clazz, UUID id) {
        String tableName = clazz.getSimpleName() + "_table";
        String query = "SELECT * FROM " + tableName + " WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setObject(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    String jsonValue = resultSet.getString("value");

                    ObjectMapper objectMapper = new ObjectMapper();
                    T instance = objectMapper.readValue(jsonValue, clazz);

                    return Optional.of(instance);
                }

                return Optional.empty();

            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Error retrieving data from the database.", e);
        }
    }

    public static <T extends AggregateRoot> void delete(T object) {
        var tableName = object.getClass().getSimpleName().toLowerCase() + "_table";
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, object.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static <T extends AggregateRoot> void insert(T object) {
        var tableName = object.getClass().getSimpleName().toLowerCase() + "_table";

        Map<String, Object> data = Serialization.serializeToMap(object);
        System.out.println("Serialized data: " + data);

        UUID id = null;
        if (data.get("id") != null) {
            id = UUID.fromString((String) data.get("id"));
        }

        UUID finalId = id;
        boolean exists = any(getAll(object.getClass()), o -> o.getId().equals(finalId));

        String sql;
        if (exists) {
            sql = "UPDATE " + tableName + " SET value = ?::jsonb WHERE id = ?";
        } else {
            sql = "INSERT INTO " + tableName + " (id, value) VALUES (?, ?::jsonb)";
        }

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonValue = objectMapper.writeValueAsString(object);

            if (exists) {
                preparedStatement.setObject(1, jsonValue, Types.OTHER);
                preparedStatement.setObject(2, id, Types.OTHER);
            } else {
                preparedStatement.setObject(1, id, Types.OTHER);
                preparedStatement.setObject(2, jsonValue, Types.OTHER);
            }

            preparedStatement.executeUpdate();
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static <T extends AggregateRoot> void insert(List<T> objects) {
        if (objects.size() == 0) {
            throw new IllegalArgumentException("The list of objects to insert must not be empty.");
        }

        if (any(objects, o -> o.getClass() != objects.get(0).getClass())) {
            throw new IllegalArgumentException("The list of objects to insert must be of the same type.");
        }

        for (var obj : objects) {
            insert(obj);
        }
    }

    public static void addManagedClass(Class<? extends AggregateRoot> clazz) {
        managedClasses.add(clazz);

        System.out.println("Added managed class: " + clazz.getSimpleName());
    }

    private static String generateTableSQL(Class<?> clazz) {
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sql.append(clazz.getSimpleName().toLowerCase() + "_table").append(" (");
        sql.append("id UUID PRIMARY KEY, value JSONB");
        sql.append(");");
        return sql.toString();
    }

    private static void createTables() {
        for (Class<? extends AggregateRoot> clazz : managedClasses) {
            String createTableSQL = generateTableSQL(clazz);
            try {
                Database.execute(createTableSQL);

                System.out.println("Created table " + clazz.getSimpleName().toLowerCase());
                System.out.println("SQL: " + createTableSQL);
                System.out.println();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}