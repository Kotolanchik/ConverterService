package ru.kolodkin.ConverterService.converterTool.converter.write;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ru.kolodkin.ConverterService.converterTool.model.Rams;
import ru.kolodkin.ConverterService.converterTool.model.RootJson;
import ru.kolodkin.ConverterService.converterTool.tool.ObjectMapperInstance;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JSONWriter implements IWriter<List<Rams>> {
    private static JSONWriter jsonWriter;

    public static synchronized JSONWriter getInstance() {
        if (jsonWriter == null) {
            jsonWriter = new JSONWriter();
        }
        return jsonWriter;
    }

    @Override
    public void write(List<Rams> model, OutputStream outputStream) {
        try {
            ObjectMapperInstance.getInstance()
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(outputStream, new RootJson(model));
        } catch (IOException exception) {
            log.fatal("Ошибка при записи json файла... ", exception);
        }
    }
}
