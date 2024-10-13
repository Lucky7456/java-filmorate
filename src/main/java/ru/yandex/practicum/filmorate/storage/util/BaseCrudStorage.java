package ru.yandex.practicum.filmorate.storage.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.storage.util.interfaces.CrudStorage;

import java.util.Map;

public abstract class BaseCrudStorage<T> extends BaseCollectionStorage<T> implements CrudStorage<T> {
    protected static final String DELETE_FROM = "DELETE FROM ";

    private final String update;
    private final String delete;

    public BaseCrudStorage(
            JdbcTemplate jdbc,
            RowMapper<T> mapper,
            String table,
            String findAllById,
            String update
    ) {
        super(jdbc, mapper, table, findAllById);
        this.update = update;
        this.delete = DELETE_FROM + table + WHERE_ID_EQUALS;
    }

    @Override
    public long create(T entity) {
        return save(toMap(entity), table);
    }

    @Override
    public int update(Object... params) {
        return update(update, params);
    }

    @Override
    public void delete(long id) {
        update(delete, id);
    }

    protected abstract Map<String, Object> toMap(T entity);
}