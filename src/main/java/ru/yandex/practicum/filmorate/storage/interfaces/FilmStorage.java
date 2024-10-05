package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> findAll();

    Collection<Film> findMostPopularFilms(int count);
    
    Film create(Film film);

    Film update(Film film);

    boolean delete(Film film);
    
    boolean addLike(long filmId, long userId);
    
    boolean removeLike(long filmId, long userId);

    Optional<Film> getFilmById(long id);
}
