package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    FilmStorage storage;

    void addLike(User user, Film film) {
        film.addLike(user);
    }

    void removeLike(User user, Film film) {
        film.removeLike(user);
    }

    List<Film> getTenMostPopularFilms() {
        return storage.findAll().stream()
                .sorted(Comparator.comparing(Film::popularity).reversed())
                .limit(10)
                .toList();
    }
}

