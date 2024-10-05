package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.BaseStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class FilmDbStorage extends BaseStorage<Film> implements FilmStorage {
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
    
    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }
    
    @Override
    public Collection<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }
    
    @Override
    public Collection<Film> findMostPopularFilms(int count) {
        return findMany(FIND_MOST_POPULAR_QUERY, count);
    }
    
    @Override
    public Film create(Film film) {
        film.setId(simpleInsert(toMap(film), TABLE_NAME));
        return film;
    }
    
    @Override
    public Film update(Film film) {
        if (update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        )) {
            return film;
        }
        throw new NotFoundException("film not found");
    }
    
    @Override
    public boolean delete(Film film) {
        return update(DELETE_QUERY, film.getId());
    }
    
    @Override
    public boolean addLike(long filmId, long userId) {
        return update(LIKE_INSERT_QUERY, filmId, userId);
    }
    
    @Override
    public boolean removeLike(long filmId, long userId) {
        return update(LIKE_DELETE_QUERY, filmId, userId);
    }
    
    @Override
    public Optional<Film> getFilmById(long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }
    
    private Map<String, Object> toMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("rating_id", film.getMpa().getId());
        return values;
    }
}
