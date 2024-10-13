package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.mappers.FilmRowMapper;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({FilmDbStorage.class, FilmRowMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FilmStorageTest {
    private final FilmStorage storage;

    @Test
    public void testFindAllFilms() {
        assertThat(storage.findAll()).hasSize(5);
    }

    @Test
    public void testFindFilmById() {
        long id = 1L;
        Optional<Film> filmOptional = storage.findOneById(id);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(
                        film -> assertThat(film)
                                .hasFieldOrPropertyWithValue("id", id)
                );
    }

    @Test
    public void testFindLimitedAmountOfFilmsSortedByPopularity() {
        int count = 3;

        assertThat(storage.findAllBy(count))
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
        film.setMpa(new RatingMpa(1L, null));
        film.setGenres(new HashSet<>());

        long id = storage.create(film);

        film.setId(id);

        assertThat(storage.findOneById(id))
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
                film.getMpa().getId(),
                film.getId()
        );

        assertThat(storage.findOneById(id)).hasValue(film);
    }

    @Test
    public void testShouldDeleteFilmSuccessfully() {
        long id = 1L;
        storage.delete(id);

        assertThat(storage.findOneById(id)).isEmpty();
    }
}