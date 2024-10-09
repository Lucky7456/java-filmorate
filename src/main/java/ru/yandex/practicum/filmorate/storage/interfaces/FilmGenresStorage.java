package ru.yandex.practicum.filmorate.storage.interfaces;

public interface FilmGenresStorage {
    int saveFilmGenre(long filmId, long genreId);

    int deleteFilmGenres(long filmId);
}