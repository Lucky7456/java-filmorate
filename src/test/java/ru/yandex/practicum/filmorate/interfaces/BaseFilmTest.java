package ru.yandex.practicum.filmorate.interfaces;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class BaseFilmTest {
    protected final Film film = new Film();

    @BeforeEach
    void setUp() {
        film.setName("film");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.parse("1895-12-28"));
        film.setDuration(1);
        film.setId(9999L);
    }
}
