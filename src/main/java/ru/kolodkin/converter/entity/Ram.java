package ru.kolodkin.converter.entity;

import lombok.*;
import org.codehaus.jackson.annotate.JsonIgnore;

@Setter
@Getter
@Builder
@NoArgsConstructor
public class Ram {
    @NonNull
    private int idRam;
    @JsonIgnore
    private String firm;
    @NonNull
    private String title;
    @NonNull
    private int releaseYear;
    @NonNull
    private Specifications specifications;

    public Ram(@NonNull int idRam, String firm, @NonNull String title, @NonNull int releaseYear, @NonNull Specifications specifications) {
        this.idRam = idRam;
        this.firm = firm;
        this.title = title;
        this.releaseYear = releaseYear;
        this.specifications = specifications;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @RequiredArgsConstructor
    public static class Specifications {
        @NonNull
        private int clockFrequency;
        @NonNull
        private int memory;
    }
}




