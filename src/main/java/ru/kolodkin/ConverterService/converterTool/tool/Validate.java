package ru.kolodkin.ConverterService.converterTool.tool;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FilenameUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Validate {
    public static boolean validateArgument(String type, String inputPath, String outputPath) {
        if (FilenameUtils.getExtension(inputPath).equals(FilenameUtils.getExtension("/src/main/document/input.xml"))
                && FilenameUtils.getExtension(outputPath).equals(FilenameUtils.getExtension("/src/main/document/output.json"))
                && type.equals("XML2JSON")) {
            return true;
        }

        if (FilenameUtils.getExtension(inputPath).equals(FilenameUtils.getExtension("/src/main/document/output.json"))
                && FilenameUtils.getExtension(outputPath).equals(FilenameUtils.getExtension("/src/main/document/input.xml"))
                && type.equals("JSON2XML")) {
            return true;
        }

        return false;
    }
}
