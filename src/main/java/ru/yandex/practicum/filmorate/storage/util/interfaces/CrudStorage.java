package ru.yandex.practicum.filmorate.storage.util.interfaces;

public interface CrudStorage<T> {
    long create(T entity);

    int update(Object... params);

    void delete(long id);
}