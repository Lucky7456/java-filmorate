package ru.yandex.practicum.filmorate.interfaces;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class BaseFilmTest {
    protected final Film film = new Film();
    protected Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        film.setName("film");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.parse("1895-12-28"));
        film.setDuration(1);
        film.setId(9999L);
    }
}
