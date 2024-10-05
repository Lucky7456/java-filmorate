package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.interfaces.BaseStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.RatingMpaStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
public class RatingMpaDbStorage extends BaseStorage<RatingMpa> implements RatingMpaStorage {
    private static final String FIND_ALL_QUERY =
            "SELECT * FROM rating_mpa";
    private static final String FIND_BY_ID_QUERY =
            "SELECT * FROM rating_mpa WHERE id = ?";
    
    public RatingMpaDbStorage(JdbcTemplate jdbc, RowMapper<RatingMpa> mapper) {
        super(jdbc, mapper);
    }
    
    @Override
    public Collection<RatingMpa> findAll() {
        return findMany(FIND_ALL_QUERY);
    }
    
    @Override
    public Optional<RatingMpa> getRatingById(int id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }
}
