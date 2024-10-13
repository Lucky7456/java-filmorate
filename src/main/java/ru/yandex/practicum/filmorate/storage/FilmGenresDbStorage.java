package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmGenresStorage;
import ru.yandex.practicum.filmorate.storage.util.BaseInsertDeleteStorage;

@Repository
public class FilmGenresDbStorage extends BaseInsertDeleteStorage implements FilmGenresStorage {
    private static final String INSERT_FILM_GENRES_QUERY =
            "INSERT INTO genres (film_id, genre_id) " +
            "VALUES (?, ?)";
    private static final String DELETE_FILM_GENRES_QUERY =
            "DELETE FROM genres " +
            "WHERE film_id = ?";

    public FilmGenresDbStorage(JdbcTemplate jdbc) {
        super(jdbc, INSERT_FILM_GENRES_QUERY, "");
    }

    @Override
    public int delete(long filmId, long ignored) {
        return update(DELETE_FILM_GENRES_QUERY, filmId);
    }
}