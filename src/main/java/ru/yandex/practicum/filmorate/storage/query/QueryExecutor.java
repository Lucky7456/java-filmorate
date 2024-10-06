package ru.yandex.practicum.filmorate.storage.query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface QueryExecutor<T> {
    Optional<T> findOne(String query, Object... params);
    
    List<T> findMany(String query, Object... params);
    
    long save(Map<String, Object> map, String tableName);
    
    int update(String query, Object... params);
    
    Optional<Integer> count(String query, Object... params);
}
