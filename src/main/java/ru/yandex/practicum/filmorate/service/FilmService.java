package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film delete(Film film) {
        return filmStorage.delete(film);
    }

    public void addLike(long filmId, long userId) {
        filmStorage.getFilmById(filmId).addLike(userStorage.getUserById(userId).getId());
    }

    public void removeLike(long filmId, long userId) {
        filmStorage.getFilmById(filmId).removeLike(userStorage.getUserById(userId).getId());
    }

    public List<Film> getTenMostPopularFilms(long count) {
        return findAll().stream()
                .sorted(Comparator.comparing(Film::popularity).reversed())
                .limit(count)
                .toList();
    }
}

