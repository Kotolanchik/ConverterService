package ru.kolodkin.converter.service.read;

import ru.kolodkin.converter.model.RootJson;
import ru.kolodkin.converter.tool.ObjectMapperInstance;

import java.io.IOException;
import java.io.InputStream;

public final class JSONReader implements IReader<RootJson> {
    @Override
    public RootJson read(InputStream inputStream) throws IOException {
        return ObjectMapperInstance.getInstance()
                .readValue(inputStream, RootJson.class);
    }
}