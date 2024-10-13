package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.mapper.FilmRowMapper;

import java.time.LocalDate;

@JdbcTest
@Import({FilmDbStorage.class, FilmRowMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FilmStorageTest {
    private final FilmStorage storage;

    @Test
    public void testFindAllFilms() {
        Assertions.assertThat(storage.findAll()).hasSize(5);
    }

    @Test
    public void testFindFilmById() {
        long id = 1L;

        Assertions.assertThat(storage.findOneById(id))
                .isPresent()
                .hasValueSatisfying(
                        film -> Assertions.assertThat(film)
                                .hasFieldOrPropertyWithValue("id", id)
                );
    }

    @Test
    public void testFindLimitedAmountOfFilmsSortedByPopularity() {
        int count = 3;

        Assertions.assertThat(storage.findAllBy(count))
                .hasOnlyElementsOfType(Film.class)
                .hasSizeLessThanOrEqualTo(count);
    }

    @Test
    public void testPersistCreatesEntityFilm() {
        Film film = new Film();
        film.setName("film");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(1);
        film.setMpa(1L);

        long id = storage.create(film);

        film.setId(id);

        Assertions.assertThat(storage.findOneById(id))
                .isPresent()
                .hasValue(film);
    }

    @Test
    public void testShouldUpdateFilmSuccessfully() {
        long id = 1L;

        Film film = storage.findOneById(id).orElseThrow();

        film.setDuration(10);

        storage.update(
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa(),
                film.getId()
        );

        Assertions.assertThat(storage.findOneById(id)).hasValue(film);
    }

    @Test
    public void testShouldDeleteFilmSuccessfully() {
        long id = 1L;
        storage.delete(id);

        Assertions.assertThat(storage.findOneById(id)).isEmpty();
    }
}