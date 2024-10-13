package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.interfaces.*;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final LikesStorage likesStorage;
    private final FilmGenresStorage filmGenresStorage;
    private final RatingMpaStorage ratingMpaStorage;

    public List<Film> findAll() {
        return prepare(filmStorage.findAll());
    }

    public List<Film> getMostPopularFilms(int count) {
        log.debug("popular films count {}", count);
        return prepare(filmStorage.findAllBy(count));
    }

    public Film create(Film film) {
        log.debug("create {}", film);
        film.setId(filmStorage.create(film));
        return process(film);
    }

    public Film update(Film film) {
        log.debug("update {}", film);
        int updatedRows = filmStorage.update(
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        if (updatedRows == 1) {
            filmGenresStorage.delete(film.getId());
            return process(film);
        }
        throw new NoSuchElementException("film not found");
    }

    public void addLike(long filmId, long userId) {
        log.debug("{} addLike {}", filmId, userId);
        likesStorage.insert(filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        log.debug("{} removeLike {}", filmId, userId);
        likesStorage.delete(filmId, userId);
    }

    public Film getFilmById(long id) {
        log.debug("getFilmById {}", id);
        return prepare(filmStorage.findOneById(id).orElseThrow());
    }

    private Film process(Film film) {
        if (Objects.nonNull(film.getGenres())) {
            filmGenresStorage.batchUpdate(film.getGenres().stream()
                    .map(genre -> new Object[]{film.getId(), genre.getId()})
                    .toList());
        }
        return film;
    }

    private Film prepare(Film film) {
        film.setMpa(ratingMpaStorage.findOneById(film.getMpa().getId()).orElseThrow());
        film.setGenres(new HashSet<>(genreStorage.findAllBy(film.getId())));
        return film;
    }

    private List<Film> prepare(List<Film> films) {
        if (films.isEmpty()) {
            return films;
        }

        List<RatingMpa> ratings = ratingMpaStorage.findAll();
        List<FilmGenre> filmGenres = filmGenresStorage.findAll();
        List<Genre> genres = genreStorage.findAll();

        return films.stream()
                .peek(film -> {
                    film.setMpa(ratings.stream()
                            .filter(rating -> rating.getId().equals(film.getMpa().getId()))
                            .findAny().orElseThrow());

                    film.setGenres(filmGenres.stream()
                            .filter(filmGenre -> filmGenre.getFilmId().equals(film.getId()))
                            .map(filmGenre -> genres.stream()
                                    .filter(genre -> genre.getId().equals(filmGenre.getGenreId()))
                                    .findAny().orElseThrow())
                            .collect(Collectors.toSet()));
                }).toList();
    }
}