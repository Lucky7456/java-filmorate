package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.storage.interfaces.FeedStorage;
import ru.yandex.practicum.filmorate.storage.util.BaseCrudStorage;

import java.util.HashMap;
import java.util.Map;

@Repository
public class FeedDbStorage extends BaseCrudStorage<Feed> implements FeedStorage {
    private static final String TABLE_NAME = "feed";
    private static final String FIND_FEED_QUERY =
            "SELECT * " +
            "FROM feed " +
            "WHERE user_id = ?";
    private static final String UPDATE_FEED_QUERY =
            "UPDATE feed " +
            "SET user_id = ?, type = ?, operation = ?, entity_id = ?, updated = ? " +
            "WHERE id = ?";

    public FeedDbStorage(JdbcTemplate jdbc, RowMapper<Feed> mapper) {
        super(jdbc, mapper, TABLE_NAME, FIND_FEED_QUERY, UPDATE_FEED_QUERY);
    }

    @Override
    protected Map<String, Object> toMap(Feed entity) {
        Map<String, Object> values = new HashMap<>();
        values.put("user_id", entity.userId());
        values.put("type", entity.eventType().name());
        values.put("operation", entity.operation().name());
        values.put("entity_id", entity.entityId());
        values.put("updated", entity.timestamp());
        return values;
    }
}
