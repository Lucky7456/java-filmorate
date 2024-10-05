package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> findAll();

    Collection<Film> findMostPopularFilms(int count);
    
    Film create(Film film);

    Film update(Film film);

    void delete(Film film);
    
    void addLike(long filmId, long userId);
    
    void removeLike(long filmId, long userId);

    Optional<Film> getFilmById(long id);
}
