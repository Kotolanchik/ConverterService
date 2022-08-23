package ru.kolodkin.converter.tool;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
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
        if (args.length != 3) {
            throw new ArrayIndexOutOfBoundsException("Неполное количество аргументов.");
        }

        if (!(StringUtils.equals(args[0], "XML2JSON") || StringUtils.equals(args[0], "JSON2XML"))) {
            throw new IllegalArgumentException(String.format("Вы ввели некорректный аргумент: %s", args[0]));
        }

        if (!(equalsIgnoreCase(getExtension(args[1]), "xml")
                || equalsIgnoreCase(getExtension(args[1]), "json"))) {
            throw new IllegalArgumentException(String.format("Вы ввели некорректный аргумент: %s", args[0]));
        }

        if (!(equalsIgnoreCase(getExtension(args[2]), ("json"))
                || equalsIgnoreCase(getExtension(args[2]), ("xml")))) {
            throw new IllegalArgumentException(String.format("Вы ввели некорректный аргумент: %s", args[2]));
        }
        log.info("С аргументами всё в порядке");
    }

    public void validateNullObject(final Object obj) {
        if (ObjectUtils.isEmpty(obj)) {
            throw new NullPointerException("Получен пустой объект.");
        }
    }

    public void checkFileExtension(final String fileName) {
        if (!(equalsIgnoreCase(getExtension(fileName), "xml")
                || equalsIgnoreCase(getExtension(fileName), "json"))) {
            throw new IllegalArgumentException("Вы пытайтесь загрузить файл с неправильным расширением.");
        }
        log.info("Расширение файла в порядке.");
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
        if (Files.notExists(Path.of(nameFile))) {
            throw new IllegalArgumentException("Файл с таким именем уже существует.");
        }
    }
}