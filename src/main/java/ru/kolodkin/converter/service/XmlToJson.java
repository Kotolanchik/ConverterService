package ru.kolodkin.converter.service;

import jakarta.xml.bind.JAXBException;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import ru.kolodkin.converter.model.Ram;
import ru.kolodkin.converter.model.Rams;
import ru.kolodkin.converter.model.RootXml;
import ru.kolodkin.converter.service.read.XMLReader;
import ru.kolodkin.converter.service.write.JSONWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;

import static java.util.stream.Collectors.toUnmodifiableSet;

@Log4j2
public final class XmlToJson implements Converter {
    private final XMLReader xmlReader = new XMLReader();
    private final JSONWriter jsonWriter = new JSONWriter();

    public void convert(InputStream input, OutputStream output) throws JAXBException, IOException {
        jsonWriter.write(
                transformXmlToJson(xmlReader.read(input)),
                output
        );
    }

    private ArrayList<Rams> transformXmlToJson(RootXml rootXml) {
        if (rootXml == null) {
            throw new NullPointerException("Пустой xml файл.");
        }

        val ramListForJson = new ArrayList<Rams>();
        for (val firm : getNameFirms(rootXml)) {
            ramListForJson.add(new Rams(firm));
        }

        for (val rams : ramListForJson) {
            for (val ram : rootXml.getRamList()) {
                if (ram.getFirm().equals(rams.getFirm())) {
                    rams.getRam().add(Ram.builder()
                            .idRam(ram.getIdRam())
                            .title(ram.getTitle())
                            .releaseYear(ram.getReleaseYear())
                            .specifications(ram.getSpecifications())
                            .build()
                    );
                }
            }
        }

        return ramListForJson;
    }

    /**
     * @param rootXml Xml модель.
     * @return Возвращает уникальные названия всех фирм из модели.
     */
    private Set<String> getNameFirms(RootXml rootXml) {
        return rootXml.getRamList()
                .stream()
                .map(Ram::getFirm)
                .collect(toUnmodifiableSet());
    }
}
