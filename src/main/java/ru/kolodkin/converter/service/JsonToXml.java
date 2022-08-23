package ru.kolodkin.converter.service;

import jakarta.xml.bind.JAXBException;
import lombok.val;
import ru.kolodkin.converter.entity.Ram;
import ru.kolodkin.converter.entity.RootJson;
import ru.kolodkin.converter.entity.RootXml;
import ru.kolodkin.converter.tool.mapper.read.JSONReader;
import ru.kolodkin.converter.tool.mapper.write.XMLWriter;
import ru.kolodkin.converter.tool.Validate;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;

public final class JsonToXml extends Converter<RootXml, RootJson> {
    public void convert(InputStream inputStream, OutputStream outputStream) throws IOException, JAXBException {
        XMLWriter.write(
                transform(JSONReader.read(inputStream)),
                outputStream
        );
    }

    RootXml transform(final RootJson rootJson) {
        Validate.validateNullObject(rootJson);

        val listRam = new ArrayList<Ram>();
        rootJson.getRams()
                .forEach(rams -> rams.getRam()
                        .forEach(ram -> listRam.add(Ram.builder()
                                .firm(rams.getFirm())
                                .specifications(ram.getSpecifications())
                                .idRam(ram.getIdRam())
                                .releaseYear(ram.getReleaseYear())
                                .title(ram.getTitle())
                                .build())));

        return new RootXml(listRam.stream()
                .sorted((comparingInt(Ram::getIdRam)))
                .collect(toList()));
    }
}