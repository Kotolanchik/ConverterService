package ru.kolodkin.ConverterService.converterTool.converter.read;

import java.io.InputStream;

public interface IReader<T> {
   T read(InputStream inputStream);
}
