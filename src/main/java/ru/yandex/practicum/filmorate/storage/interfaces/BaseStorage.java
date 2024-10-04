package ru.yandex.practicum.filmorate.storage.interfaces;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class BaseStorage<T> {
    protected final JdbcTemplate jdbc;
    protected final RowMapper<T> mapper;
    
    protected Optional<T> findOne(String query, Object... params) {
        try {
            T result = jdbc.queryForObject(query, mapper, params);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }
    
    protected List<T> findMany(String query, Object... params) {
        return jdbc.query(query, mapper, params);
    }
    
    protected long simpleInsert(Map<String, Object> map, String tableName) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbc)
                .withTableName(tableName)
                .usingGeneratedKeyColumns("id");
        return simpleJdbcInsert.executeAndReturnKey(map).longValue();
    }
    
    protected boolean update(String query, Object... params) {
        if (jdbc.update(query, params) > 0) {
            return true;
        }
        throw new RuntimeException("Не удалось обновить данные");
    }
    
    protected Integer exists(String query, Object... params) {
        return jdbc.queryForObject(query, Integer.class, params);
    }
}