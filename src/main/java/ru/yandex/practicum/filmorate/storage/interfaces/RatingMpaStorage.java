package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.Collection;
import java.util.Optional;

public interface RatingMpaStorage {
    Collection<RatingMpa> findAll();
    
    Optional<RatingMpa> getRatingById(int id);
}
