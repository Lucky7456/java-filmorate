package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.util.BaseCrudStorage;

import java.util.HashMap;
import java.util.Map;

@Repository
public class FilmDbStorage extends BaseCrudStorage<Film> implements FilmStorage {
    private static final String TABLE_NAME = "films";
    private static final String FIND_MOST_POPULAR_QUERY =
            "SELECT f.* " +
            "FROM films AS f " +
            "LEFT JOIN likes AS l ON l.film_id = f.id " +
            "GROUP BY f.id " +
            "ORDER BY COUNT(l.user_id) DESC " +
            "LIMIT ?";
    private static final String UPDATE_QUERY =
            "UPDATE films " +
            "SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? " +
            "WHERE id = ?";

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper, TABLE_NAME, FIND_MOST_POPULAR_QUERY, UPDATE_QUERY);
    }

    @Override
    protected Map<String, Object> toMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("rating_id", film.getMpa());
        return values;
    }
}
