package ru.yandex.practicum.filmorate.storage.interfaces;

public interface CrudStorage<T> {
    long create(T entity);
    
    boolean update(Object... params);
    
    void delete(long id);
}