package ru.kolodkin.converter.entity;

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


