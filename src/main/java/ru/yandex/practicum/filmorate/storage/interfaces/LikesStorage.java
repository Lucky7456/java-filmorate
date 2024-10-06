package ru.yandex.practicum.filmorate.storage.interfaces;

public interface LikesStorage {
    void addLike(long filmId, long userId);
    
    void removeLike(long filmId, long userId);
}
