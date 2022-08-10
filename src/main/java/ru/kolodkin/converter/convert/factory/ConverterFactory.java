package ru.kolodkin.converter.convert.factory;

import ru.kolodkin.converter.service.Converter;
import ru.kolodkin.converter.service.JsonToXml;
import ru.kolodkin.converter.service.XmlToJson;

public final class ConverterFactory {
    public static Converter createConverter(ConverterType type) {
        if (type == ConverterType.JSON2XML) {
            return new JsonToXml();
        }
        return new XmlToJson();
    }
}
