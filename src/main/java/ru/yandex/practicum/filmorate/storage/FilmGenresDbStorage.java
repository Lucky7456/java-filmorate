package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmGenresStorage;
import ru.yandex.practicum.filmorate.storage.util.query.BaseQueryExecutor;

import java.util.List;

@Repository
public class FilmGenresDbStorage extends BaseQueryExecutor<FilmGenre> implements FilmGenresStorage {
    private static final String FIND_ALL_QUERY =
            "SELECT * FROM genres ";
    private static final String INSERT_FILM_GENRES_QUERY =
            "INSERT INTO genres (film_id, genre_id) " +
            "VALUES (?, ?)";
    private static final String DELETE_FILM_GENRES_QUERY =
            "DELETE FROM genres " +
            "WHERE film_id = ?";

    public FilmGenresDbStorage(JdbcTemplate jdbc, RowMapper<FilmGenre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<FilmGenre> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public int[] batchUpdate(List<Object[]> batch){
        return batchUpdate(INSERT_FILM_GENRES_QUERY, batch);
    }

    @Override
    public int delete(long filmId) {
        return update(DELETE_FILM_GENRES_QUERY, filmId);
    }
}