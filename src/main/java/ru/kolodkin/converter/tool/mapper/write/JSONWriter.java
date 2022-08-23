package ru.kolodkin.converter.tool.mapper.write;

import ru.kolodkin.converter.entity.Rams;
import ru.kolodkin.converter.entity.RootJson;
import ru.kolodkin.converter.tool.ObjectMapperInstance;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public final class JSONWriter {
    public static void write(final List<Rams> model,final OutputStream outputStream) throws IOException {
        ObjectMapperInstance.getObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValue(outputStream, new RootJson(model));
    }
}
