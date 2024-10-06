package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.storage.interfaces.CollectionStorage;

import java.util.List;

public abstract class BaseCollectionStorage<T> extends BaseEntityStorage<T> implements CollectionStorage<T> {
    protected final String findAllById;

    public BaseCollectionStorage(
            JdbcTemplate jdbc,
            RowMapper<T> mapper,
            String findAll,
            String findOneById,
            String findAllById
    ) {
        super(jdbc, mapper, findAll, findOneById);
        this.findAllById = findAllById;
    }

    @Override
    public List<T> findAllBy(long id) {
        return findMany(findAllById, id);
    }
}