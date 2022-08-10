package ru.kolodkin.converter.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class Rams {
    @NonNull
    private String firm;
    @NonNull
    private List<Ram> ram = new ArrayList<>();
}


