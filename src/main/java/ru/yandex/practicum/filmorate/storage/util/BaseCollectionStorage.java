package ru.yandex.practicum.filmorate.storage.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.storage.util.interfaces.CollectionStorage;

import java.util.List;

public abstract class BaseCollectionStorage<T> extends BaseEntityStorage<T> implements CollectionStorage<T> {
    protected final String findAllById;

    public BaseCollectionStorage(
            JdbcTemplate jdbc,
            RowMapper<T> mapper,
            String table,
            String findAllById
    ) {
        super(jdbc, mapper, table);
        this.findAllById = findAllById;
    }

    @Override
    public List<T> findAllBy(long id) {
        return findMany(findAllById, id);
    }
}