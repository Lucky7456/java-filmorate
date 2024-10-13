package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.interfaces.BaseFilmTest;

import java.time.LocalDate;
import java.util.Set;

public class FilmTest extends BaseFilmTest {
    @Test
    void shouldFailEmptyFilm() {
        Film emptyFilm = new Film();
        Set<ConstraintViolation<Film>> violations = validator.validate(emptyFilm);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(4, violations.size());
    }

    @Test
    void shouldFailNullOrBlankName() {
        film.setName(null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());

        film.setName("");
        violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
    }

    @Test
    void shouldFailDescriptionLongerThan200() {
        String longText = "1".repeat(201);
        film.setDescription(longText);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
    }

    @Test
    void shouldFailReleaseDateBefore1895Dec28() {
        film.setReleaseDate(LocalDate.parse("1895-12-27"));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
    }

    @Test
    void shouldFailNegativeOrZeroDuration() {
        film.setDuration(0);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());

        film.setDuration(-1);
        violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
    }

    @Test
    void shouldSucceedCreatingValidFilm() {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertTrue(violations.isEmpty());
    }
}