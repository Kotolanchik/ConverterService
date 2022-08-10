package ru.kolodkin.converter.controller;

import jakarta.xml.bind.JAXBException;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.kolodkin.converter.exception.BadRequestException;
import ru.kolodkin.converter.service.ConverterService;
import ru.kolodkin.converter.tool.Validate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;


@RestController
public class ConverterController {
    @Value("${upload.path}")
    private String uploadPath;
    @Autowired
    private ConverterService converterService;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("Вам не удалось загрузить " + file.getOriginalFilename() + ", потому не выбран.");
        }

        if(!(Validate.checkXmlExtension(file.getOriginalFilename()).right
                || Validate.checkJsonExtension(file.getOriginalFilename()).right)){
            throw new BadRequestException("Вы пытайтесь загрузить файл с неправильным расширением.");
        }

        return converterService.upload(file);
    }

    @GetMapping("/convert")
    public String convert(@RequestParam("convertType") String converterType,
                                  @RequestParam("firstFile") String firstFile,
                                  @RequestParam("secondFile") String secondFile) throws IOException, JAXBException {
        val validArgument = Validate.validateArgument(converterType, firstFile, secondFile);
        if (!validArgument.right) {
            throw new BadRequestException(validArgument.left);
        }

        if (Files.notExists(Path.of(uploadPath + firstFile))) {
            throw new FileNotFoundException("Файл для конвертации не загружен на сервер.");
        }

        return converterService.convert(converterType, firstFile, secondFile);
    }

    @GetMapping("/download")
    public ResponseEntity download(@RequestParam("nameFile") String nameFile) throws FileNotFoundException {
        if (StringUtils.isEmpty(nameFile)) {
            throw new IllegalArgumentException("Пустой запрос.");
        }

        if (Files.notExists(Path.of(uploadPath + nameFile))) {
            throw new FileNotFoundException("Файл: " + nameFile + " на сервере отсутствует.");
        }

        FileSystemResource resource = new FileSystemResource(uploadPath + nameFile);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition
                .builder("attachment")
                .filename(Objects.requireNonNull(resource.getFilename()))
                .build()
        );

        return new ResponseEntity(resource, headers, HttpStatus.OK);
    }

    @GetMapping("/files")
    public String getAllFile() {
        return converterService.getAllFile();
    }

    @PostMapping("/convert/unique")
    public ResponseEntity uniqueConvert(@RequestParam("convertType") String converterType,
                                        @RequestParam("firstFile") MultipartFile firstFile,
                                        @RequestParam("secondFile") String secondFile) throws IOException, JAXBException {
        String fileName = firstFile.getOriginalFilename();
        val validArgument = Validate.validateArgument(converterType, fileName, secondFile);
        if (!validArgument.right) {
            throw new IllegalArgumentException(validArgument.left);
        }

        if (firstFile.isEmpty()) {
            throw new BadRequestException("Вам не удалось загрузить " + fileName + ", потому что файл пустой.");
        }

        return converterService.uniqueConvert(converterType, firstFile, secondFile);
    }
}
