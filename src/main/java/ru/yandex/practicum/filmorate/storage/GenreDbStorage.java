package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.BaseStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
public class GenreDbStorage extends BaseStorage<Genre> implements GenreStorage {
    private static final String FIND_ALL_QUERY =
            "SELECT * FROM genre";
    private static final String FIND_BY_ID_QUERY =
            "SELECT * FROM genre WHERE id = ?";
    private static final String FIND_FILM_GENRES_QUERY =
            "SELECT g.* " +
            "FROM genre AS g " +
            "JOIN genres AS gs ON g.id = gs.genre_id " +
            "WHERE gs.film_id = ?";
    private static final String FILM_EXISTS_QUERY =
            "SELECT * FROM genres WHERE film_id = ? LIMIT 1";
    
    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }
    
    @Override
    public Collection<Genre> findAll() {
        return findMany(FIND_ALL_QUERY);
    }
    
    @Override
    public Collection<Genre> findFilmGenres(long filmId) {
        if (exists(FILM_EXISTS_QUERY, filmId).orElse(0) == 1) {
            return findMany(FIND_FILM_GENRES_QUERY, filmId);
        }
        throw new NotFoundException("film not found");
    }
    
    @Override
    public Optional<Genre> getGenreById(int id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }
}
