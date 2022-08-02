package ru.kolodkin.ConverterService;

import jakarta.xml.bind.JAXBException;
import lombok.val;
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
import ru.kolodkin.ConverterService.converterTool.factory.ConverterFactory;
import ru.kolodkin.ConverterService.converterTool.factory.ConverterType;
import ru.kolodkin.ConverterService.converterTool.tool.Validate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class ConverterServiceController {
    private static final String pathDocumentConvert = "src/main/java/ru/kolodkin/ConverterService/convertDocument/";

    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) {
        String name = file.getOriginalFilename();
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Вам не удалось загрузить " + name + ", потому что файл пустой.");
        }

        try (BufferedOutputStream stream = new BufferedOutputStream(
                new FileOutputStream(pathDocumentConvert + file.getOriginalFilename()))) {
            stream.write(file.getBytes());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Вы удачно загрузили " + name + "!");
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Вам не удалось загрузить " + name + " => " + exception.getMessage());
        }
    }

    @GetMapping("/convert")
    public ResponseEntity convert(@RequestParam("convertType") String converterType,
                                        @RequestParam("firstFile") String firstFile,
                                        @RequestParam("secondFile") String secondFile) {
        if (!Validate.validateArgument(converterType, firstFile, secondFile)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Входные аргументы некорректны.");
        }

        if (Files.notExists(Path.of(pathDocumentConvert + firstFile))) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Файл для конвертации не загружен на сервер.");
        }

        try (val inputStream = new FileInputStream(pathDocumentConvert + firstFile);
             val outputStream = new FileOutputStream(pathDocumentConvert + secondFile)) {

            ConverterFactory.createConverter(ConverterType.valueOf(converterType))
                    .convert(inputStream, outputStream);

        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Вам не удалось конвертировать файл => " + exception.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("Конвертация прошла успешно.");
    }

    @GetMapping("/download")
    public ResponseEntity downloadResult(@RequestParam("nameFile") String nameFile) {
        if(nameFile.equals("")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Пустой запрос.");
        }

        if (Files.notExists(Path.of(pathDocumentConvert + nameFile))) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Файл: " + nameFile + " на сервере отсутствует!");
        }

        FileSystemResource resource = new FileSystemResource(pathDocumentConvert + nameFile);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition
                .builder("attachment")
                .filename(Objects.requireNonNull(resource.getFilename()))
                .build()
        );

        return new ResponseEntity(resource, headers, HttpStatus.OK);
    }

    @GetMapping("/files")
    public ResponseEntity getAllFile() {
        val files = Arrays.stream(Objects.requireNonNull(new File(pathDocumentConvert).listFiles()))
                .map(File::getName)
                .collect(Collectors.toList());

        if (files.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Нет файлов.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(files.toString());
    }

    @PostMapping("/convert/unique")
    public ResponseEntity uniqueConvert(@RequestParam("convertType") String converterType,
                                        @RequestParam("firstFile") MultipartFile firstFile,
                                        @RequestParam("secondFile") String secondFile) {
        String fileName = firstFile.getOriginalFilename();
        if (!Validate.validateArgument(converterType, fileName, secondFile)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Входные аргументы некорректны.");
        }

        if (firstFile.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    "Вам не удалось загрузить " + fileName + ", потому что файл пустой.");
        }

        try (BufferedOutputStream stream = new BufferedOutputStream(
                new FileOutputStream(pathDocumentConvert + fileName));) {
            stream.write(firstFile.getBytes());
        } catch (IOException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Вам не удалось загрузить " + fileName + " => " + exception.getMessage());
        }

        try (val inputStream = new FileInputStream(pathDocumentConvert + fileName);
             val outputStream = new FileOutputStream(pathDocumentConvert + secondFile)) {

            ConverterFactory.createConverter(ConverterType.valueOf(converterType))
                    .convert(inputStream, outputStream);

        } catch (IOException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Вам не удалось конвертировать файл => " + exception.getMessage());
        } catch (JAXBException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ошибка при преобразовании содержания файла в объект. Проверьте правильность ваших данных непосредственно в файле.");
        }

        FileSystemResource resource = new FileSystemResource(pathDocumentConvert + secondFile);
        HttpHeaders headers = new HttpHeaders();
        ContentDisposition disposition = ContentDisposition
                .builder("attachment")
                .filename(Objects.requireNonNull(resource.getFilename()))
                .build();
        headers.setContentDisposition(disposition);
        return new ResponseEntity(resource, headers, HttpStatus.OK);
    }
}
