package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.storage.util.interfaces.InsertDeleteStorage;

public interface ReviewLikesStorage extends InsertDeleteStorage {
    int exists(long reviewId, long userId);
}
