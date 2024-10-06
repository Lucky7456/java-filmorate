package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.storage.interfaces.CrudStorage;

import java.util.Map;

public abstract class BaseCrudStorage<T> extends BaseCollectionStorage<T> implements CrudStorage<T> {
    private final String table;
    private final String update;
    private final String delete;
    
    public BaseCrudStorage(
            JdbcTemplate jdbc,
            RowMapper<T> mapper,
            String findAll,
            String findOneById,
            String findAllById,
            String table,
            String update,
            String delete
    ) {
        super(jdbc, mapper, findAll, findOneById, findAllById);
        this.table = table;
        this.update = update;
        this.delete = delete;
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
