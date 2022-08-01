package ru.kolodkin.ConverterService.converterTool.converter.read;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ru.kolodkin.ConverterService.converterTool.model.RootXml;

import java.io.InputStream;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class XMLReader implements IReader <RootXml> {
    private static XMLReader xmlReader;

    public static synchronized XMLReader getInstance() {
        if (xmlReader == null) {
            xmlReader = new XMLReader();
        }
        return xmlReader;
    }

    @Override
    public RootXml read(InputStream inputStream) {
        try {
            return (RootXml) JAXBContext.newInstance(RootXml.class)
                    .createUnmarshaller()
                    .unmarshal(inputStream);
        } catch (JAXBException exception) {
            log.fatal("Ошибка при преобразование xml файла в объект... ", exception);
        }
        return null;
    }
}
