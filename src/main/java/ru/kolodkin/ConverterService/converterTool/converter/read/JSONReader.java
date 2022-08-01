package ru.kolodkin.ConverterService.converterTool.converter.read;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ru.kolodkin.ConverterService.converterTool.model.RootJson;
import ru.kolodkin.ConverterService.converterTool.tool.ObjectMapperInstance;

import java.io.IOException;
import java.io.InputStream;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JSONReader implements IReader<RootJson> {
    private static JSONReader jsonReader;

    public static synchronized JSONReader getInstance() {
        if (jsonReader == null) {
            jsonReader = new JSONReader();
        }
        return jsonReader;
    }

    @Override
    public RootJson read(InputStream inputStream) {
        try {
            return ObjectMapperInstance.getInstance()
                    .readValue(inputStream, RootJson.class);
        } catch (IOException exception) {
            log.fatal("Ошибка при чтении json файла..", exception);
        }
        return null;
    }
}