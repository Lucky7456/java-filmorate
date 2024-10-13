package ru.yandex.practicum.filmorate.storage.util;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.storage.util.query.BaseUpdateExecutor;
import ru.yandex.practicum.filmorate.storage.util.interfaces.InsertDeleteStorage;

public abstract class BaseInsertDeleteStorage extends BaseUpdateExecutor implements InsertDeleteStorage {
    private final String insert;
    private final String delete;

    public BaseInsertDeleteStorage(JdbcTemplate jdbc, String insert, String delete) {
        super(jdbc);
        this.insert = insert;
        this.delete = delete;
    }

    @Override
    public int insert(long entityId, long otherId) {
        return update(insert, entityId, otherId);
    }

    @Override
    public int delete(long entityId, long otherId) {
        return update(delete, entityId, otherId);
    }
}