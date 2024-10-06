package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.SQLDataException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.RatingMpaStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
        film.setId(filmStorage.create(film));
        
        if (Objects.nonNull(film.getGenres())) {
            saveFilmGenres(film.getId(), film.getGenres());
            film.setGenres(new ArrayList<>(genreStorage.findAllBy(film.getId())));
        }
        
        film.setMpa(
                ratingMpaStorage.findOneById(film.getMpa().getId())
                        .orElseThrow(() -> new SQLDataException("rating not found"))
        );
        
        return film;
    }

    public Film update(Film film) {
        log.debug("update {}", film);
        if (filmStorage.update(
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        ) != 1) {
            throw new NotFoundException("film not found");
        }
        
        if (Objects.nonNull(film.getGenres())) {
            filmStorage.deleteFilmGenres(film.getId());
            saveFilmGenres(film.getId(), film.getGenres());
            film.setGenres(new ArrayList<>(genreStorage.findAllBy(film.getId())));
        }
        
        film.setMpa(
                ratingMpaStorage.findOneById(film.getMpa().getId())
                        .orElseThrow(() -> new NotFoundException("rating not found"))
        );
        
        return film;
    }

    public void addLike(long filmId, long userId) {
        filmStorage.addLike(filmId, userId);
        log.debug("{} addLike {}", filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        filmStorage.removeLike(filmId, userId);
        log.debug("{} removeLike {}", filmId, userId);
    }

    public Collection<Film> getMostPopularFilms(int count) {
        log.debug("popular films count {}", count);
        return filmStorage.findAllBy(count);
    }
    
    public Film getFilmById(long id) {
        log.debug("getFilmById {}", id);
        Film film = filmStorage.findOneById(id)
                .orElseThrow(() -> new NotFoundException("film not found"));
        
        film.setMpa(ratingMpaStorage.findOneById(film.getMpa().getId())
                .orElseThrow(() -> new NotFoundException("rating not found")));
        
        film.setGenres(new ArrayList<>(genreStorage.findAllBy(id)));
        
        return film;
    }
    
    private void saveFilmGenres(long filmId, List<Genre> genres) {
        for (Genre genre : genres) {
            log.trace("film {} save {} genre", filmId, genre.getId());
            if (genreStorage.findOneById(genre.getId()).isEmpty()) {
                throw new SQLDataException("bad request");
            }
            filmStorage.saveFilmGenre(filmId, genre.getId());
        }
    }
}