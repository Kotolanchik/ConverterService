package ru.kolodkin.converter.tool;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.nio.file.*;

import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;

@Slf4j
@UtilityClass
public final class Validate {
    public void validateArgument(final String[] args) {
        final String inCorrectStr = "Вы ввели некорректный аргумент: %s";
        StringBuilder errorMessage = new StringBuilder();
        boolean check = true;

        if (args.length != 2) {
            errorMessage.append("Неполное количество аргументов.");
            check = false;
        }

        if (!(StringUtils.equals(args[0], "XML2JSON") || StringUtils.equals(args[0], "JSON2XML"))) {
            errorMessage.append(String.format(inCorrectStr, args[0]));
            check = false;
        }

        if (!(equalsIgnoreCase(getExtension(args[1]), "xml")
                || equalsIgnoreCase(getExtension(args[1]), "json"))) {
            errorMessage.append(String.format(inCorrectStr, args[1]));
            check = false;
        }

        if (!check) {
            throw new IllegalArgumentException(errorMessage.toString());
        }
    }

    public void checkFileExtension(final String fileName) {
        if (!(equalsIgnoreCase(getExtension(fileName), "xml")
                || equalsIgnoreCase(getExtension(fileName), "json"))) {
            throw new IllegalArgumentException("Вы пытайтесь загрузить файл с неправильным расширением.");
        }
    }

    public void checkFileNotExistOnServer(final String pathFile) throws FileNotFoundException {
        if (Files.notExists(Path.of(pathFile))) {
            throw new FileNotFoundException("Такого файла нет на сервере.");
        }
        log.info(String.format("Файл %s присутствует на сервере", pathFile));
    }

    public void checkUploadFileOnServer(final MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException(String.format("Вам не удалось загрузить %s, потому что произведена загрузка пустого файла.", file.getOriginalFilename()));
        }
    }

    public void checkEmptyRequest(final String request) {
        if (StringUtils.isEmpty(request)) {
            throw new IllegalArgumentException("Пустой запрос.");
        }
    }

    public void checkFileExistOnServer(String nameFile) {
        if (Files.exists(Path.of(nameFile))) {
            throw new IllegalArgumentException("Такого файла нет.");
        }
    }
}