package ru.yandex.practicum.filmorate.storage.util.interfaces;

import java.util.List;
import java.util.Optional;

public interface EntityStorage<T> {
    List<T> findAll();

    Optional<T> findOneById(long id);
}