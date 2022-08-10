package ru.kolodkin.converter.tool;

import org.codehaus.jackson.map.ObjectMapper;

public final class ObjectMapperInstance {
    private static ObjectMapper INSTANCE;

    private ObjectMapperInstance() {
    }

    public static synchronized ObjectMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ObjectMapper();
        }
        return INSTANCE;
    }
}
