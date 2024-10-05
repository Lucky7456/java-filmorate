package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.PastDate;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class Film {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    @Size(max = 200)
    private String description;
    @PastDate("1895-12-28")
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private RatingMpa mpa;
    private List<Genre> genres;
}
