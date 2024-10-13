package ru.yandex.practicum.filmorate.storage.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.storage.util.query.BaseQueryExecutor;
import ru.yandex.practicum.filmorate.storage.util.interfaces.EntityStorage;

import java.util.List;
import java.util.Optional;

public abstract class BaseEntityStorage<T> extends BaseQueryExecutor<T> implements EntityStorage<T> {
    protected static final String SELECT_FROM = "SELECT * FROM ";
    protected static final String WHERE_ID_EQUALS = " WHERE id = ?";

    protected final String table;
    protected final String findAll;
    protected final String findOneById;

    public BaseEntityStorage(
            JdbcTemplate jdbc,
            RowMapper<T> mapper,
            String table
    ) {
        super(jdbc, mapper);
        this.table = table;
        this.findAll = SELECT_FROM + table;
        this.findOneById = SELECT_FROM + table + WHERE_ID_EQUALS;
    }

    @Override
    public List<T> findAll() {
        return findMany(findAll);
    }

    @Override
    public Optional<T> findOneById(long id) {
        return findOne(findOneById, id);
    }
}