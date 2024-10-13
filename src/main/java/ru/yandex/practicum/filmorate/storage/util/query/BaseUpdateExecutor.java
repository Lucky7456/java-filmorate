package ru.yandex.practicum.filmorate.storage.util.query;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
public abstract class BaseUpdateExecutor implements UpdateExecutor {
    final JdbcTemplate jdbc;

    @Override
    public int update(String query, Object... params) {
        return jdbc.update(query, params);
    }
}