package ru.yandex.practicum.filmorate.storage.interfaces;

import java.util.Collection;
import java.util.Optional;

public interface EntityStorage<T> {
    Collection<T> findAll();
    
    Optional<T> findOneById(long id);
}
