package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    Collection<Genre> findAll();
    
    Collection<Genre> findByFilmId(long filmId);
    
    Optional<Genre> getGenreById(int id);
    
    void saveFilmGenres(Film film, List<Genre> genres);
    
    void deleteFilmGenres(Long filmId);
}
