package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.storage.interfaces.EntityStorage;
import ru.yandex.practicum.filmorate.storage.query.BaseQueryExecutor;

import java.util.Collection;
import java.util.Optional;

public abstract class BaseEntityStorage<T> extends BaseQueryExecutor<T> implements EntityStorage<T> {
    protected final String findAll;
    protected final String findOneById;
    
    public BaseEntityStorage(
            JdbcTemplate jdbc,
            RowMapper<T> mapper,
            String findAll,
            String findOneById
    ) {
        super(jdbc, mapper);
        this.findAll = findAll;
        this.findOneById = findOneById;
    }
    
    @Override
    public Collection<T> findAll() {
        return findMany(findAll);
    }
    
    @Override
    public Optional<T> findOneById(long id) {
        return findOne(findOneById, id);
    }
}
