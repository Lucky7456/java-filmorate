package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmGenresStorage;
import ru.yandex.practicum.filmorate.storage.mapper.FilmGenreRowMapper;

import java.util.Collections;

@JdbcTest
@Import({FilmGenresDbStorage.class, FilmGenreRowMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FilmGenresStorageTest {
    private final FilmGenresStorage storage;

    @Test
    public void testFindAllFilmGenres() {
        Assertions.assertThat(storage.findAll()).hasSize(12);
    }

    @Test
    public void testShouldSaveFilmGenre() {
        long filmId = 1L;
        long genreId = 1L;

        Assertions.assertThat(storage.batchUpdate(Collections.singletonList((new Object[]{filmId, genreId})))).contains(1);
    }

    @Test
    public void testShouldDeleteFilmGenres() {
        long filmId = 3L;

        Assertions.assertThat(storage.delete(filmId)).isGreaterThanOrEqualTo(1);
    }
}