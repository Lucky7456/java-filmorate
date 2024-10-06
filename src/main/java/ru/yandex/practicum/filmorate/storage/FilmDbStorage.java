package ru.yandex.practicum.filmorate.storage;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.HashMap;
import java.util.Map;

@Repository
public class FilmDbStorage extends BaseCrudStorage<Film> implements FilmStorage {
    private static final String TABLE_NAME = "films";
    private static final String FIND_ALL_QUERY =
            "SELECT f.*, r.rating " +
            "FROM films AS f " +
            "JOIN rating_mpa AS r ON r.id = f.rating_id";
    private static final String FIND_MOST_POPULAR_QUERY =
            "SELECT f.*, r.rating " +
            "FROM films AS f " +
            "JOIN rating_mpa AS r ON r.id = f.rating_id " +
            "JOIN likes AS l ON l.film_id = f.id " +
            "GROUP BY l.film_id " +
            "ORDER BY COUNT(l.user_id) DESC " +
            "LIMIT ?";
    private static final String UPDATE_QUERY =
            "UPDATE films SET name = ?, " +
            "description = ?, release_date = ?, duration = ?, rating_id = ? " +
            "WHERE id = ?";
    private static final String DELETE_QUERY =
            "DELETE FROM films WHERE id = ?";
    private static final String LIKE_INSERT_QUERY =
            "INSERT INTO likes (film_id, user_id) " +
            "VALUES (?, ?)";
    private static final String LIKE_DELETE_QUERY =
            "DELETE FROM likes " +
            "WHERE film_id = ? AND user_id = ?";
    private static final String FIND_BY_ID_QUERY =
            "SELECT f.*, r.rating " +
            "FROM films AS f " +
            "JOIN rating_mpa AS r ON r.id = f.rating_id " +
            "WHERE f.id = ?";
    private static final String INSERT_FILM_GENRES_QUERY =
            "INSERT INTO genres (film_id, genre_id) " +
                    "VALUES (?, ?)";
    private static final String DELETE_FILM_GENRES_QUERY =
            "DELETE FROM genres WHERE film_id = ?";
    
    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper, FIND_ALL_QUERY, FIND_BY_ID_QUERY, FIND_MOST_POPULAR_QUERY, TABLE_NAME, UPDATE_QUERY, DELETE_QUERY);
    }
    
    @Override
    public void addLike(long filmId, long userId) {
        update(LIKE_INSERT_QUERY, filmId, userId);
    }
    
    @Override
    public void removeLike(long filmId, long userId) {
        update(LIKE_DELETE_QUERY, filmId, userId);
    }
    
    @Override
    public void deleteFilmGenres(long filmId) {
        update(DELETE_FILM_GENRES_QUERY, filmId);
    }
    
    @Override
    public void saveFilmGenre(long filmId, int genreId) {
        try {
            update(INSERT_FILM_GENRES_QUERY, filmId, genreId);
        } catch (DataAccessException ignored) {
        
        }
    }
    
    @Override
    protected Map<String, Object> toMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("rating_id", film.getMpa().getId());
        return values;
    }
}
