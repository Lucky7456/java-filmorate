package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;

public interface FilmGenresStorage {
    List<FilmGenre> findAll();

    int[] batchUpdate(List<Object[]> batch);

    int delete(long filmId);
}