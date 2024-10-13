package ru.yandex.practicum.filmorate.storage.util.query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface QueryExecutor<T> extends UpdateExecutor {
    Optional<T> findOne(String query, Object... params);

    List<T> findMany(String query, Object... params);

    long save(Map<String, Object> map, String tableName);

    int[] batchUpdate(String query, List<Object[]> batch);
}