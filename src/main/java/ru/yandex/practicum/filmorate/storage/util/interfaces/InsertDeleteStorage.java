package ru.yandex.practicum.filmorate.storage.util.interfaces;

public interface InsertDeleteStorage {
    int insert(long entityId, long otherId);

    int delete(long entityId, long otherId);
}