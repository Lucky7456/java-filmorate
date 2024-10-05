package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        log.debug("create {}", film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        log.debug("update {}", film);
        return filmStorage.update(film);
    }

    public boolean delete(Film film) {
        log.debug("delete {}", film);
        return filmStorage.delete(film);
    }

    public void addLike(long filmId, long userId) {
        filmStorage.addLike(filmId, userId);
        log.debug("{} addLike {}", filmStorage.getFilmById(filmId), userStorage.getUserById(userId));
    }

    public void removeLike(long filmId, long userId) {
        filmStorage.removeLike(filmId, userId);
        log.debug("{} removeLike {}", filmStorage.getFilmById(filmId), userStorage.getUserById(userId));
    }

    public Collection<Film> getTenMostPopularFilms(int count) {
        log.debug("popular films count {}", count);
        return filmStorage.findMostPopularFilms(count);
    }
}