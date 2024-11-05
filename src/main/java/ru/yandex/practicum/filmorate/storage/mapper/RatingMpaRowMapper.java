package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RatingMpaRowMapper implements RowMapper<RatingMpa> {
    @Override
    public RatingMpa mapRow(ResultSet rs, int ignored) throws SQLException {
        return new RatingMpa(
                rs.getLong("id"),
                rs.getString("rating")
        );
    }
}