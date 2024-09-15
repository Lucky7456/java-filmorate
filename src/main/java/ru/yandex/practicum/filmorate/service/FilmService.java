package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage storage;

    public Collection<Film> findAll() {
        return storage.findAll();
    }

    public Film create(Film film) {
        return storage.create(film);
    }

    public Film update(Film film) {
        return storage.update(film);
    }

    public Film delete(Film film) {
        return storage.delete(film);
    }

    public void addLike(User user, Film film) {
        film.addLike(user);
    }

    public void removeLike(User user, Film film) {
        film.removeLike(user);
    }

    public List<Film> getTenMostPopularFilms() {
        return findAll().stream()
                .sorted(Comparator.comparing(Film::popularity).reversed())
                .limit(10)
                .toList();
    }
}

