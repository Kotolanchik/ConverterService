package ru.kolodkin.converter.service.write;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import lombok.val;
import ru.kolodkin.converter.model.RootXml;

import java.io.OutputStream;

public final class XMLWriter implements IWriter<RootXml> {
    @Override
    public void write(RootXml model, OutputStream outputStream) throws JAXBException {
        val jaxbMarshaller = JAXBContext.newInstance(RootXml.class).createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(model, outputStream);
    }
}
