package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.FilmDirector;

import java.util.List;

public interface FilmDirectorStorage {
    List<FilmDirector> findAll();

    int[] batchUpdate(List<Object[]> batch);

    int delete(long filmId);
}
