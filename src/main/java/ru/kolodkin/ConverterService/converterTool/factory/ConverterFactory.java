package ru.kolodkin.ConverterService.converterTool.factory;

import ru.kolodkin.ConverterService.converterTool.converter.Converter;
import ru.kolodkin.ConverterService.converterTool.converter.JsonToXml;
import ru.kolodkin.ConverterService.converterTool.converter.XmlToJson;

public final class ConverterFactory {
    public static Converter createConverter(ConverterType type) {
        if (type == ConverterType.JSON2XML) {
            return new JsonToXml();
        }
        return new XmlToJson();
    }
}
