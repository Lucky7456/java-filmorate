package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

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
        return filmStorage.findAll().stream().map(this::prepare).toList();
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
            filmGenresStorage.delete(film.getId(), 0);
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

    public List<Film> getMostPopularFilms(int count) {
        log.debug("popular films count {}", count);
        return filmStorage.findAllBy(count).stream().map(this::prepare).toList();
    }

    public Film getFilmById(long id) {
        log.debug("getFilmById {}", id);
        return prepare(filmStorage.findOneById(id).orElseThrow());
    }

    private Film prepare(Film film) {
        film.setMpa(ratingMpaStorage.findOneById(film.getMpa().getId()).orElseThrow());
        film.setGenres(genreStorage.findAllBy(film.getId()));
        return film;
    }

    private Film process(Film film) {
        film.setMpa(ratingMpaStorage.findOneById(film.getMpa().getId())
                .orElseThrow(() -> new DataIntegrityViolationException("bad request")));
        if (Objects.nonNull(film.getGenres())) {
            saveFilmGenres(film.getId(), film.getGenres());
            film.setGenres(genreStorage.findAllBy(film.getId()));
        }
        return film;
    }

    private void saveFilmGenres(long filmId, List<Genre> genres) {
        for (Genre genre : genres) {
            log.trace("film {} save {} genre", filmId, genre.getId());
            genreStorage.findOneById(genre.getId())
                    .orElseThrow(() -> new DataIntegrityViolationException("bad request"));
            try {
                filmGenresStorage.insert(filmId, genre.getId());
            } catch (DataAccessException e) {
                log.warn(e.getMessage());
            }
        }
    }
}