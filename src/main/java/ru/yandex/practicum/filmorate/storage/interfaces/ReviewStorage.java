package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.util.interfaces.CollectionStorage;
import ru.yandex.practicum.filmorate.storage.util.interfaces.CrudStorage;

import java.util.List;

public interface ReviewStorage extends
        CollectionStorage<Review>,
        CrudStorage<Review> {
    List<Review> findAllBy(long filmId, long count);
}
