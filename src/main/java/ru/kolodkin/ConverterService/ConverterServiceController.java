package ru.kolodkin.ConverterService;

import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kolodkin.ConverterService.converterTool.factory.ConverterFactory;
import ru.kolodkin.ConverterService.converterTool.factory.ConverterType;
import ru.kolodkin.ConverterService.converterTool.tool.Validate;

import java.io.*;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@Log4j2
public class ConverterServiceController {
    final static private String pathDocumentConvert = "src/main/java/ru/kolodkin/ConverterService/convertDocument/";
    @PostMapping("/upload")
    public @ResponseBody String uploadFile(@RequestParam("file") MultipartFile file) {
        String name = file.getOriginalFilename();
        if (!file.isEmpty()) {
            try (BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(pathDocumentConvert + file.getOriginalFilename()));) {
                byte[] bytes = file.getBytes();
                stream.write(bytes);
                return "Вы удачно загрузили " + name + "!";
            } catch (Exception e) {
                return "Вам не удалось загрузить " + name + " => " + e.getMessage();
            }
        } else {
            return "Вам не удалось загрузить " + name + " потому что файл пустой.";
        }
    }

    @GetMapping("/convert")
    public @ResponseBody String convert(@RequestParam("convertType") String converterType,
                                        @RequestParam("firstFile") String firstFile,
                                        @RequestParam("secondFile") String secondFile) {
        if (!Validate.validateArgument(converterType, firstFile, secondFile)) {
            log.error("Входные аргументы некорректны.");
            return "Входные аргументы некорректны.";
        }

        log.info("С расширениями всё в порядке.");

        try (val inputStream = new FileInputStream(pathDocumentConvert + firstFile);
             val outputStream = new FileOutputStream(pathDocumentConvert + secondFile)) {

            ConverterFactory.createConverter(ConverterType.valueOf(converterType))
                    .convert(inputStream, outputStream);
            log.info("Конвертация прошла успешно.");
        } catch (FileNotFoundException exception) {
            log.fatal("Файл не найден... " + exception);
        } catch (IllegalArgumentException exception) {
            log.fatal("Неправильный ввод входных данных... ", exception);
        } catch (Exception exception) {
            log.fatal("Непредвиденная ошибка... ", exception);
        }
        return "Конвертация прошла успешно";
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadResult(@RequestParam("nameFile") String nameFile) {
        FileSystemResource resource = new FileSystemResource(pathDocumentConvert + nameFile);
        HttpHeaders headers = new HttpHeaders();
        ContentDisposition disposition = ContentDisposition
                .builder("attachment")
                .filename(resource.getFilename())
                .build();
        headers.setContentDisposition(disposition);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @GetMapping("/allFile")
    public String getAllFile() {
        return Arrays.stream(new File(pathDocumentConvert).listFiles())
                .map(File::getName)
                .collect(Collectors.toList())
                .toString();
    }
}
