package com.idis.shared.serialization;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public final class Serialization {
    private static final ObjectMapper objectMapper = getObjectMapper();

    private static ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        SimpleModule module = new SimpleModule();
        module.addDeserializer(UUID.class, new UUIDDeserializer());
        mapper.registerModule(module);

        return mapper;
    }

    /**
     * Serializes the specified object to a JSON string.
     *
     * @param object the object to serialize
     * @return a JSON string representation of the object
     */
    public static String serialize(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException("Error during serialization", e);
        }
    }

    /**
     * Deserializes the specified JSON string to an object of the specified target class.
     *
     * @param json        the JSON string to deserialize
     * @param targetClass the class of the object to deserialize to
     * @param <T>         the type of the object to deserialize to
     * @return an instance of the target class created from the JSON string
     */
    public static <T> T deserialize(String json, Class<T> targetClass) {
        try {
            return objectMapper.readValue(json, targetClass);
        } catch (IOException e) {
            throw new RuntimeException("Error during deserialization", e);
        }
    }

    /**
     * Deserializes the specified map to an object of the specified target class.
     *
     * @param data        the map to deserialize
     * @param targetClass the class of the object to deserialize to
     * @param <T>         the type of the object to deserialize to
     * @return an instance of the target class created from the map
     */
    public static <T> T deserialize(Map<String, Object> data, Class<T> targetClass) {
        return objectMapper.convertValue(data, targetClass);
    }

    /**
     * Serializes the specified object to a map with string keys and object values.
     *
     * @param object the object to serialize
     * @return a map containing the serialized object's data
     */
    public static Map<String, Object> serializeToMap(Object object) {
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        MapType mapType = typeFactory.constructMapType(Map.class, String.class, Object.class);
        return objectMapper.convertValue(object, mapType);
    }
}
