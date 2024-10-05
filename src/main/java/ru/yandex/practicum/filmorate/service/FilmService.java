package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.SQLDataException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.RatingMpaStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final RatingMpaStorage ratingMpaStorage;

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        log.debug("create {}", film);
        Film createdFilm = filmStorage.create(film);
        
        if (Objects.nonNull(film.getGenres())) {
            genreStorage.saveFilmGenres(film, film.getGenres());
            createdFilm.setGenres(new ArrayList<>(genreStorage.findByFilmId(film.getId())));
        }
        
        createdFilm.setMpa(
                ratingMpaStorage.getRatingById(film.getMpa().getId())
                        .orElseThrow(() -> new SQLDataException("rating not found"))
        );
        
        return film;
    }

    public Film update(Film film) {
        log.debug("update {}", film);
        Film updatedFilm = filmStorage.update(film);
        
        if (Objects.nonNull(film.getGenres())) {
            genreStorage.deleteFilmGenres(film.getId());
            genreStorage.saveFilmGenres(film, film.getGenres());
            updatedFilm.setGenres(new ArrayList<>(genreStorage.findByFilmId(film.getId())));
        }
        
        updatedFilm.setMpa(
                ratingMpaStorage.getRatingById(film.getMpa().getId())
                        .orElseThrow(() -> new NotFoundException("rating not found"))
        );
        
        return updatedFilm;
    }

    public void addLike(long filmId, long userId) {
        filmStorage.addLike(filmId, userId);
        log.debug("{} addLike {}", filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        filmStorage.removeLike(filmId, userId);
        log.debug("{} removeLike {}", filmId, userId);
    }

    public Collection<Film> getTenMostPopularFilms(int count) {
        log.debug("popular films count {}", count);
        return filmStorage.findMostPopularFilms(count);
    }
    
    public Film getFilmById(long id) {
        Film film = filmStorage.getFilmById(id)
                .orElseThrow(() -> new NotFoundException("film not found"));
        
        film.setMpa(ratingMpaStorage.getRatingById(film.getMpa().getId())
                .orElseThrow(() -> new NotFoundException("rating not found")));
        
        film.setGenres(new ArrayList<>(genreStorage.findByFilmId(id)));
        
        return film;
    }
}