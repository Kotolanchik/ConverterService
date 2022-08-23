package ru.kolodkin.converter.tool.mapper.read;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import ru.kolodkin.converter.entity.RootXml;

import java.io.InputStream;

public final class XMLReader {
    public static RootXml read(final InputStream inputStream) throws JAXBException {
        return (RootXml) JAXBContext.newInstance(RootXml.class)
                .createUnmarshaller()
                .unmarshal(inputStream);
    }
}
