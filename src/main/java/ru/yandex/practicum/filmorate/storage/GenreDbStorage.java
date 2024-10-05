package ru.yandex.practicum.filmorate.storage;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.SQLDataException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.BaseStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.util.Collection;
import java.util.List;
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
    private static final String INSERT_FILM_GENRES_QUERY =
            "INSERT INTO genres (film_id, genre_id) " +
                    "VALUES (?, ?)";
    private static final String DELETE_FILM_GENRES_QUERY =
            "DELETE FROM genres WHERE film_id = ?";
    
    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }
    
    @Override
    public Collection<Genre> findAll() {
        return findMany(FIND_ALL_QUERY);
    }
    
    @Override
    public Collection<Genre> findByFilmId(long filmId) {
        return findMany(FIND_FILM_GENRES_QUERY, filmId);
    }
    
    @Override
    public Optional<Genre> getGenreById(int id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }
    
    @Override
    public void saveFilmGenres(Film film, List<Genre> genres) {
        for (Genre genre : genres) {
            saveFilmGenre(film.getId(), genre.getId());
        }
    }
    
    @Override
    public void deleteFilmGenres(Long filmId) {
        update(DELETE_FILM_GENRES_QUERY, filmId);
    }
    
    private void saveFilmGenre(long filmId, int genreId) {
        if (getGenreById(genreId).isEmpty()) {
            throw new SQLDataException("bad request");
        }
        try {
            update(INSERT_FILM_GENRES_QUERY, filmId, genreId);
        } catch (DataAccessException ignored) {
        
        }
    }
}
