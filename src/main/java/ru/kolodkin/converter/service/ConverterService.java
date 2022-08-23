package ru.kolodkin.converter.service;

import jakarta.xml.bind.JAXBException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kolodkin.converter.convert.factory.ConverterFactory;
import ru.kolodkin.converter.convert.factory.ConverterType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class ConverterService {
    @Value("${upload.path}")
    private String uploadPath;

    public String upload(MultipartFile file) {
        try (val inputStream = file.getInputStream()) {
            FileUtils.copyInputStreamToFile(inputStream, new File(uploadPath + file.getOriginalFilename()));

            return "Файл загружен.";
        } catch (IOException exception) {
            log.error("Непредвиденная ошибка: ", exception);
            return "Возникла ошибка при загрузке файла " + file.getOriginalFilename() + ", проверьте его корректность.";
        }
    }

    public String convert(String converterType, String firstFile, String secondFile) throws IOException, JAXBException {
        try (val inputStream = new FileInputStream(uploadPath + firstFile);
             val outputStream = new FileOutputStream(uploadPath + secondFile)) {

            ConverterFactory
                    .createConverter(ConverterType.valueOf(converterType))
                    .convert(inputStream, outputStream);

        } catch (IOException | JAXBException exception) {
            log.error("Непредвиденная ошибка: ", exception);
            return "Файл не удалось конвертировать.";
        }
        return "Конвертация прошла успешно. Файл сохранён на сервере.";
    }

    public String getAllFile() {
        try (val stream = Files.walk(Paths.get(uploadPath))) {
            val files = stream
                    .map(file -> file.getFileName().toString())
                    .collect(toList());

            if (CollectionUtils.isNotEmpty(files)) {
                return files.toString();
            }
        } catch (IOException exception) {
            log.error("Непредвиденная ошибка: ", exception);
        }
        return "Нет файлов.";
    }

    public byte[] uniqueConvert(String converterType, MultipartFile firstFile) throws IOException, JAXBException {
        try (val inputStream = firstFile.getInputStream();
             val byteArrayOutputStream = new ByteArrayOutputStream()) {

            log.info("Начало конвертации.");

            ConverterFactory.createConverter(ConverterType.valueOf(converterType))
                    .convert(inputStream, byteArrayOutputStream);

            log.info("Конец конвертации.");
            return byteArrayOutputStream.toByteArray();
        } catch (IOException exception) {
            throw new IOException("Непредвиденная ошибка:");
        }
    }
}
