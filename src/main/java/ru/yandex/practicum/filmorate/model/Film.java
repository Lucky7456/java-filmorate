package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.PastDate;

import java.time.LocalDate;

@Data
public class Film {
    Long id;
    @NotBlank
    String name;
    @Size(max = 200)
    String description;
    @PastDate
    LocalDate releaseDate;
    @Positive
    int duration;
}
