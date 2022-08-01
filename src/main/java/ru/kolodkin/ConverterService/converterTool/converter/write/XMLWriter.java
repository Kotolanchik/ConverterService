package ru.kolodkin.ConverterService.converterTool.converter.write;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import ru.kolodkin.ConverterService.converterTool.model.RootXml;

import java.io.OutputStream;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class XMLWriter implements IWriter<RootXml> {
    private static XMLWriter xmlWriter;

    public static synchronized XMLWriter getInstance() {
        if (xmlWriter == null) {
            xmlWriter = new XMLWriter();
        }
        return xmlWriter;
    }

    @Override
    public void write(RootXml model, OutputStream outputStream) {
        try {
            val jaxbMarshaller = JAXBContext.newInstance(RootXml.class).createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(model, outputStream);
        } catch (JAXBException exception) {
            log.fatal("Ошибка при преобразование объектов в документ xml... ", exception);
        }
    }
}
