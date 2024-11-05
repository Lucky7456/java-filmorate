package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.util.interfaces.CollectionStorage;
import ru.yandex.practicum.filmorate.storage.util.interfaces.CrudStorage;

import java.util.List;

public interface FilmStorage extends
        CollectionStorage<Film>,
        CrudStorage<Film> {
    List<Film> search(String query, List<String> by);
}
