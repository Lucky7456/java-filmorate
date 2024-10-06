package ru.yandex.practicum.filmorate.storage.interfaces;

import java.util.Optional;

public interface CrudStorage<T> {
    long create(T entity);
    
    Optional<T> findOneById(long id);
    
    boolean update(Object... params);
    
    void delete(long id);
}