package ru.kolodkin.converter.service;

import jakarta.xml.bind.JAXBException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kolodkin.converter.tool.factory.ConverterFactory;
import ru.kolodkin.converter.tool.factory.ConverterType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class ConverterService {
    @Value("${upload.path}")
    private String uploadPath;

    public String upload(MultipartFile file) {
        try (val inputStream = file.getInputStream()) {
            String uniqueID = UUID.randomUUID().toString();
            FileUtils.copyInputStreamToFile(
                    inputStream,
                    new File(uploadPath + file.getName() + uniqueID + "." + FilenameUtils.getExtension(file.getOriginalFilename()))
            );

            return String.format("Файл загружен. Используйте имя: %s для его конвертации и скачивания.",
                    file.getName() + uniqueID + "." + FilenameUtils.getExtension(file.getOriginalFilename()));
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

            log.info("Конвертация прошла успешно.");
        } catch (IOException | JAXBException exception) {
            log.error("Непредвиденная ошибка: ", exception);
            return "Файл не удалось конвертировать.";
        }
        return String.format("Конвертация прошла успешно. Файл сохранён на сервере. Имя файла: %s", secondFile);
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
            ConverterFactory.createConverter(ConverterType.valueOf(converterType))
                    .convert(inputStream, byteArrayOutputStream);

            log.info("Конвертация прошла успешно.");
            return byteArrayOutputStream.toByteArray();
        }
    }

    @Scheduled(cron = "* 30 14 15 * ?")
    public void periodDeleteFiles() throws IOException {
        try (val stream = Files.walk(Paths.get(uploadPath))) {
            val files = stream.collect(toList());

            if (CollectionUtils.isNotEmpty(files)) {
                files.forEach(path -> FileUtils.deleteQuietly(new File(String.valueOf(path))));
                log.info("Файлы удалены с сервера.");
            }
        }
    }
}
