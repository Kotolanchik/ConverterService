package ru.kolodkin.converter.service.read;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import ru.kolodkin.converter.model.RootXml;

import java.io.InputStream;


public final class XMLReader implements IReader<RootXml> {
    @Override
    public RootXml read(InputStream inputStream) throws JAXBException {
        return (RootXml) JAXBContext.newInstance(RootXml.class)
                .createUnmarshaller()
                .unmarshal(inputStream);
    }
}
