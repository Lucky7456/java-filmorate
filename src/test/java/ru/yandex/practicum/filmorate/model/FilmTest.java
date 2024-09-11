package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.interfaces.BaseFilmTest;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmTest extends BaseFilmTest {
    @Autowired
    private Validator validator;

    @Test
    void shouldFailEmptyFilm() {
        Film emptyFilm = new Film();
        Set<ConstraintViolation<Film>> violations = validator.validate(emptyFilm);
        assertFalse(violations.isEmpty());
        assertEquals(4, violations.size());
    }

    @Test
    void shouldFailNullOrBlankName() {
        film.setName(null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        film.setName("");
        violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailDescriptionLongerThan200() {
        String longText = "1234567890".repeat(20);
        film.setDescription(longText + " ");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailReleaseDateBefore1895Dec28() {
        film.setReleaseDate(LocalDate.parse("1895-12-27"));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailNegativeOrZeroDuration() {
        film.setDuration(0);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        film.setDuration(-1);
        violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    void shouldSucceedCreatingValidFilm() {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }
}
