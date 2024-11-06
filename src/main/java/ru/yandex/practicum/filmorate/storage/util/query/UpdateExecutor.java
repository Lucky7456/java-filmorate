package ru.yandex.practicum.filmorate.storage.util.query;

public interface UpdateExecutor {
    int update(String query, Object... params);
    Integer count(String query, Object... params);
}
