package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

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

    public Film delete(Film film) {
        log.debug("delete {}", film);
        return filmStorage.delete(film);
    }

    public void addLike(long filmId, long userId) {
        filmStorage.getFilmById(filmId).addLike(userStorage.getUserById(userId).getId());
        log.debug("{} addLike {}", filmStorage.getFilmById(filmId), userStorage.getUserById(userId));
    }

    public void removeLike(long filmId, long userId) {
        filmStorage.getFilmById(filmId).removeLike(userStorage.getUserById(userId).getId());
        log.debug("{} removeLike {}", filmStorage.getFilmById(filmId), userStorage.getUserById(userId));
    }

    public List<Film> getTenMostPopularFilms(long count) {
        log.debug("popular films count {}", count);
        return findAll().stream()
                .sorted(Comparator.comparing(Film::popularity).reversed())
                .limit(count)
                .toList();
    }
}

