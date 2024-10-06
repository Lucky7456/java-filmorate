package ru.yandex.practicum.filmorate.storage.interfaces;

public interface FilmGenresStorage {
    void saveFilmGenre(long filmId, int genreId);
    
    void deleteFilmGenres(long filmId);
}
