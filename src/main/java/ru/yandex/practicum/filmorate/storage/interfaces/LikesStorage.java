package ru.yandex.practicum.filmorate.storage.interfaces;

public interface LikesStorage {
    int addLike(long filmId, long userId);

    int removeLike(long filmId, long userId);
}