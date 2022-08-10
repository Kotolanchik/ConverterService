package ru.kolodkin.converter.service;

import jakarta.xml.bind.JAXBException;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import ru.kolodkin.converter.model.Ram;
import ru.kolodkin.converter.model.RootJson;
import ru.kolodkin.converter.model.RootXml;
import ru.kolodkin.converter.service.read.JSONReader;
import ru.kolodkin.converter.service.write.XMLWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;

import static java.util.stream.Collectors.toList;

@Log4j2
public final class JsonToXml implements Converter {
    private final JSONReader jsonReader = new JSONReader();
    private final XMLWriter xmlWriter = new XMLWriter();

    public void convert(InputStream inputStream, OutputStream outputStream) throws IOException, JAXBException {
        xmlWriter.write(
                transformJsonToXml(jsonReader.read(inputStream)),
                outputStream
        );
    }

    private RootXml transformJsonToXml(RootJson rootJson) {
        if (rootJson == null) {
            throw new NullPointerException("Пустой json файл.");
        }

        val listRam = new ArrayList<Ram>();
        for (int rootIndex = 0; rootIndex < rootJson.getRams().size(); rootIndex++) {
            for (int ramsIndex = 0; ramsIndex < rootJson.getRams().get(rootIndex).getRam().size(); ramsIndex++) {
                listRam.add(Ram.builder()
                        .firm(rootJson.getRams().get(rootIndex).getFirm())
                        .specifications(rootJson.getRams().get(rootIndex).getRam().get(ramsIndex).getSpecifications())
                        .idRam(rootJson.getRams().get(rootIndex).getRam().get(ramsIndex).getIdRam())
                        .releaseYear(rootJson.getRams().get(rootIndex).getRam().get(ramsIndex).getReleaseYear())
                        .title(rootJson.getRams().get(rootIndex).getRam().get(ramsIndex).getTitle())
                        .build()
                );
            }
        }

        return new RootXml(listRam.stream()
                .sorted((Comparator.comparingInt(Ram::getIdRam)))
                .collect(toList()));
    }
}
