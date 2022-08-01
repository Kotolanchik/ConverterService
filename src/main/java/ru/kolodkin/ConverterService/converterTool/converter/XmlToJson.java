package ru.kolodkin.ConverterService.converterTool.converter;

import lombok.extern.log4j.Log4j2;
import lombok.val;
import ru.kolodkin.ConverterService.converterTool.converter.read.XMLReader;
import ru.kolodkin.ConverterService.converterTool.converter.write.JSONWriter;
import ru.kolodkin.ConverterService.converterTool.model.Ram;
import ru.kolodkin.ConverterService.converterTool.model.Rams;
import ru.kolodkin.ConverterService.converterTool.model.RootXml;


import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
public final class XmlToJson implements Converter {
    public void convert(InputStream input, OutputStream output) {
        JSONWriter.getInstance().write(
                transformXmlToJson(XMLReader.getInstance().read(input)),
                output
        );
    }

    private ArrayList<Rams> transformXmlToJson(RootXml rootXml) {
        if(rootXml == null) {
            log.error("Пустой xml файл.");
            return null;
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
                .collect(Collectors.toSet());
    }
}
