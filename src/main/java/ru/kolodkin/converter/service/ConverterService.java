package ru.kolodkin.converter.service;

import jakarta.xml.bind.JAXBException;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kolodkin.converter.convert.factory.ConverterFactory;
import ru.kolodkin.converter.convert.factory.ConverterType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.util.stream.Collectors.toList;

@Service
@Log4j2
public class ConverterService {
    @Value("${upload.path}")
    private String uploadPath;

    public String upload(MultipartFile file) {
        try (val inputStream = file.getInputStream()) {
            FileUtils.copyInputStreamToFile(inputStream, new File(uploadPath + file.getOriginalFilename()));

            return "Файл загружен.";
        } catch (IOException exception) {
            exception.printStackTrace();
            return "Возникла ошибка при загрузке файла " + file.getOriginalFilename() + ", проверьте его корректность.";
        }
    }

    public String convert(String converterType, String firstFile, String secondFile) throws IOException, JAXBException {
        try (val inputStream = new FileInputStream(uploadPath + firstFile);
             val outputStream = new FileOutputStream(uploadPath + secondFile)) {

            ConverterFactory
                    .createConverter(ConverterType.valueOf(converterType))
                    .convert(inputStream, outputStream);

        } catch (IOException exception) {
            exception.printStackTrace();
            throw new IOException("Проблема с вводом-выводом.");
        } catch (JAXBException exception) {
            exception.printStackTrace();
            throw new JAXBException("Проблема при парсинге.");
        }

        return "Конвертация прошла успешно. Файл был сохранён на сервере.";
    }

    public String getAllFile() {
        try {
            val files = Files.walk(Paths.get(uploadPath))
                    .map(file -> file.getFileName().toString())
                    .collect(toList());

            if (files.size() != 0) {
                return files.toString();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return "Нет файлов.";
    }

    public ResponseEntity uniqueConvert(String converterType, MultipartFile firstFile, String secondFile) throws IOException, JAXBException {
        try (val inputStream = firstFile.getInputStream();
             val byteArrayOutputStream = new ByteArrayOutputStream()) {

            log.info("Начало конвертации.");

            ConverterFactory.createConverter(ConverterType.valueOf(converterType))
                    .convert(inputStream, byteArrayOutputStream);

            log.info("Конец конвертации.");
            HttpHeaders headers = new HttpHeaders();
            ContentDisposition disposition = ContentDisposition
                    .builder("attachment")
                    .filename(secondFile)
                    .build();
            headers.setContentDisposition(disposition);
            return new ResponseEntity(byteArrayOutputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new IOException("Проблема с вводом-выводом.");
        } catch (JAXBException exception) {
            exception.printStackTrace();
            throw new JAXBException("Проблема при парсинге.");
        }
    }
}
