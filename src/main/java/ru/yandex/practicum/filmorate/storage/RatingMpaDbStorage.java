package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.interfaces.RatingMpaStorage;
import ru.yandex.practicum.filmorate.storage.util.BaseEntityStorage;

@Repository
public class RatingMpaDbStorage extends BaseEntityStorage<RatingMpa> implements RatingMpaStorage {
    private static final String TABLE_NAME = "rating_mpa";

    public RatingMpaDbStorage(JdbcTemplate jdbc, RowMapper<RatingMpa> mapper) {
        super(jdbc, mapper, TABLE_NAME);
    }
}