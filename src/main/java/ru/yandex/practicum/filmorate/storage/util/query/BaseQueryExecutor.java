package ru.yandex.practicum.filmorate.storage.util.query;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class BaseQueryExecutor<T> extends BaseUpdateExecutor implements QueryExecutor<T> {
    private final RowMapper<T> mapper;

    public BaseQueryExecutor(JdbcTemplate jdbc, RowMapper<T> mapper) {
        super(jdbc);
        this.mapper = mapper;
    }

    @Override
    public Optional<T> findOne(String query, Object... params) {
        try {
            return Optional.ofNullable(jdbc.queryForObject(query, mapper, params));
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public List<T> findMany(String query, Object... params) {
        return jdbc.query(query, mapper, params);
    }

    @Override
    public long save(Map<String, Object> map, String tableName) {
        return new SimpleJdbcInsert(jdbc)
                .withTableName(tableName)
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(map)
                .longValue();
    }

    @Override
    public int[] batchUpdate(String query, List<Object[]> batch) {
        return jdbc.batchUpdate(query, batch);
    }
}