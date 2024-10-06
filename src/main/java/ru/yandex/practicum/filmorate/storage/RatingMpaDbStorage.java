package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.interfaces.RatingMpaStorage;

@Repository
public class RatingMpaDbStorage extends BaseEntityStorage<RatingMpa> implements RatingMpaStorage {
    private static final String FIND_ALL_QUERY =
            "SELECT * FROM rating_mpa";
    private static final String FIND_BY_ID_QUERY =
            "SELECT * FROM rating_mpa WHERE id = ?";

    public RatingMpaDbStorage(JdbcTemplate jdbc, RowMapper<RatingMpa> mapper) {
        super(jdbc, mapper, FIND_ALL_QUERY, FIND_BY_ID_QUERY);
    }
}