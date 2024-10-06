package ru.yandex.practicum.filmorate.storage.query;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class BaseQueryExecutor<T> implements QueryExecutor<T>{
    protected final JdbcTemplate jdbc;
    protected final RowMapper<T> mapper;
    
    @Override
    public Optional<T> findOne(String query, Object... params) {
        try {
            T result = jdbc.queryForObject(query, mapper, params);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }
    
    @Override
    public Collection<T> findMany(String query, Object... params) {
        return jdbc.query(query, mapper, params);
    }
    
    @Override
    public long save(Map<String, Object> map, String tableName) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbc)
                .withTableName(tableName)
                .usingGeneratedKeyColumns("id");
        return simpleJdbcInsert.executeAndReturnKey(map).longValue();
    }
    
    @Override
    public int update(String query, Object... params) {
        return jdbc.update(query, params);
    }
    
    @Override
    public Optional<Integer> count(String query, Object... params) {
        try {
            Integer result = jdbc.queryForObject(query, Integer.class, params);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }
}