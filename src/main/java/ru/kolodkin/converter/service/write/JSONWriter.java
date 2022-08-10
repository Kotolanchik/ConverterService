package ru.kolodkin.converter.service.write;


import ru.kolodkin.converter.model.Rams;
import ru.kolodkin.converter.model.RootJson;
import ru.kolodkin.converter.tool.ObjectMapperInstance;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public final class JSONWriter implements IWriter<List<Rams>> {
    @Override
    public void write(List<Rams> model, OutputStream outputStream) throws IOException {
        ObjectMapperInstance.getInstance()
                .writerWithDefaultPrettyPrinter()
                .writeValue(outputStream, new RootJson(model));
    }
}
