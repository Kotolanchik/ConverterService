package ru.kolodkin.converter.service;

import jakarta.xml.bind.JAXBException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Converter<T, S> {
    void convert(InputStream input, OutputStream output) throws IOException, JAXBException;

    T transform(S root);
}
