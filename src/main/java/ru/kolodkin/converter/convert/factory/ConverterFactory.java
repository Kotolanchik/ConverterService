package ru.kolodkin.converter.convert.factory;

import lombok.experimental.UtilityClass;
import ru.kolodkin.converter.service.Converter;
import ru.kolodkin.converter.service.JsonToXml;
import ru.kolodkin.converter.service.XmlToJson;

@UtilityClass
public final class ConverterFactory {
    public Converter createConverter(final ConverterType type) {
        return type == ConverterType.JSON2XML ? new JsonToXml() : new XmlToJson();
    }
}
