package ru.kolodkin.converter.tool.mapper;

import lombok.experimental.UtilityClass;
import org.codehaus.jackson.map.ObjectMapper;

@UtilityClass
public final class ObjectMapperInstance {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}


