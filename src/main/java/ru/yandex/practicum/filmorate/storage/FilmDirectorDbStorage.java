package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmDirector;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmDirectorStorage;
import ru.yandex.practicum.filmorate.storage.util.query.BaseQueryExecutor;

import java.util.List;

@Repository
public class FilmDirectorDbStorage extends BaseQueryExecutor<FilmDirector> implements FilmDirectorStorage {
    private static final String FIND_ALL_QUERY =
            "SELECT * FROM directors";
    private static final String INSERT_FILM_DIRECTORS_QUERY =
            "INSERT INTO directors (film_id, director_id) " +
            "VALUES (?, ?)";
    private static final String DELETE_FILM_DIRECTORS_QUERY =
            "DELETE FROM directors " +
            "WHERE film_id = ?";

    public FilmDirectorDbStorage(JdbcTemplate jdbc, RowMapper<FilmDirector> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<FilmDirector> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public int[] batchUpdate(List<Object[]> batch) {
        return batchUpdate(INSERT_FILM_DIRECTORS_QUERY, batch);
    }

    @Override
    public int delete(long filmId) {
        return update(DELETE_FILM_DIRECTORS_QUERY, filmId);
    }
}
