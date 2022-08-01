package ru.kolodkin.ConverterService.converterTool.model;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class RootJson {
    @NonNull
    private List<Rams> rams;
}
