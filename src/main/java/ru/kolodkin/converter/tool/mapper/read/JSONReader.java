package ru.kolodkin.converter.tool.mapper.read;

import ru.kolodkin.converter.entity.RootJson;
import ru.kolodkin.converter.tool.ObjectMapperInstance;

import java.io.IOException;
import java.io.InputStream;

public final class JSONReader  {
    public static RootJson read(final InputStream inputStream) throws IOException {
        return ObjectMapperInstance.getObjectMapper()
                .readValue(inputStream, RootJson.class);
    }
}