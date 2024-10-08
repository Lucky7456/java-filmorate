package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage extends
        CollectionStorage<Film>,
        CrudStorage<Film>,
        FilmGenresStorage,
        LikesStorage {
}
