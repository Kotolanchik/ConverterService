package ru.kolodkin.converter.service;

import jakarta.xml.bind.JAXBException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class Converter<T, S> {
    public abstract void convert(InputStream input, OutputStream output) throws IOException, JAXBException;

    abstract T transform(S root);
}
