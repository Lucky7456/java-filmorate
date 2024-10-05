package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RatingMpaRowMapper implements RowMapper<RatingMpa> {
    @Override
    public RatingMpa mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new RatingMpa(
                rs.getInt("id"),
                rs.getString("rating")
        );
    }
}
