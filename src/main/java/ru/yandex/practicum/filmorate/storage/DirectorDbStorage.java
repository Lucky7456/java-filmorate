package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.interfaces.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.util.BaseCrudStorage;

import java.util.HashMap;
import java.util.Map;

@Repository
public class DirectorDbStorage extends BaseCrudStorage<Director> implements DirectorStorage {
    private static final String TABLE_NAME = "director";
    private static final String FIND_BY_FILM_QUERY =
            "SELECT d.* " +
            "FROM director AS d " +
            "JOIN directors AS ds ON d.id = ds.director_id " +
            "WHERE ds.film_id = ?";
    private static final String UPDATE_QUERY =
            "UPDATE director " +
            "SET name = ? " +
            "WHERE id = ?";

    public DirectorDbStorage(JdbcTemplate jdbc, RowMapper<Director> mapper) {
        super(jdbc, mapper, TABLE_NAME, FIND_BY_FILM_QUERY, UPDATE_QUERY);
    }

    @Override
    protected Map<String, Object> toMap(Director entity) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", entity.name());
        return values;
    }
}
