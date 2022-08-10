package ru.kolodkin.converter.service.read;

import jakarta.xml.bind.JAXBException;

import java.io.IOException;
import java.io.InputStream;

public interface IReader<T> {
    T read(InputStream inputStream) throws IOException, JAXBException;
}
