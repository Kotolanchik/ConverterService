package ru.kolodkin.converter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kolodkin.converter.service.ConverterService;
import ru.kolodkin.converter.tool.Validate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Api("Контроллер для работы с конвертером и файлами сервера.")
public class ConverterController {
    @Value("${upload.path}")
    private String uploadPath;
    private final ConverterService converterService;

    @PostMapping("/upload")
    @ApiOperation("Загрузка файла на сервер")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        Validate.checkUploadFileOnServer(file);
        Validate.checkFileExtension(file.getOriginalFilename());
        Validate.checkFileExistOnServer(uploadPath + file.getOriginalFilename());

        return converterService.upload(file);
    }

    @GetMapping("/convert")
    @ApiOperation("Конвертация файла с загрузкой и сохранением результата на сервере")
    public String convert(@RequestParam("convertType") String converterType,
                          @RequestParam("firstFile") String firstFile) throws IOException, JAXBException {
        Validate.validateArgument(new String[]{converterType, firstFile});
        Validate.checkFileNotExistOnServer(uploadPath + firstFile);

        return converterService.convert(
                converterType,
                firstFile,
                String.format(
                        "%s.%s",
                        UUID.randomUUID().toString(),
                        StringUtils.equals(converterType, "XML2JSON") ? "json" : "xml"
                )
        );
    }

    @GetMapping("/download")
    @ApiOperation("Сохранение файла на личном устройстве")
    public ResponseEntity download(@RequestParam("nameFile") String nameFile) throws FileNotFoundException {
        Validate.checkEmptyRequest(nameFile);
        Validate.checkFileNotExistOnServer(uploadPath + nameFile);

        FileSystemResource resource = new FileSystemResource(uploadPath + nameFile);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition
                .builder("attachment")
                .filename(Objects.requireNonNull(resource.getFilename()))
                .build());

        return new ResponseEntity(resource, headers, HttpStatus.OK);
    }

    @GetMapping("/files")
    @ApiOperation("Получение списка файлов доступных для скачивания")
    public String getAllFile() {
        return converterService.getAllFile();
    }

    @PostMapping("/convert/unique")
    @ApiOperation("Конвертация файлов без сохранения на сервере")
    public ResponseEntity uniqueConvert(@RequestParam("convertType") String converterType,
                                        @RequestParam("firstFile") MultipartFile firstFile) throws IOException, JAXBException {
        Validate.validateArgument(new String[]{converterType, firstFile.getOriginalFilename()});
        Validate.checkUploadFileOnServer(firstFile);

        HttpHeaders headers = new HttpHeaders();
        ContentDisposition disposition = ContentDisposition
                .builder("attachment")
                .filename(String.format(
                        "%s.%s",
                        UUID.randomUUID().toString(),
                        StringUtils.equals(converterType, "XML2JSON") ? "json" : "xml")
                )
                .build();
        headers.setContentDisposition(disposition);

        return new ResponseEntity(converterService.uniqueConvert(converterType, firstFile), headers, HttpStatus.OK);
    }
}