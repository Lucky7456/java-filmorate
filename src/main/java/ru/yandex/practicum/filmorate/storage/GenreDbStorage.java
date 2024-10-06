package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

@Repository
public class GenreDbStorage extends BaseCollectionStorage<Genre> implements GenreStorage {
    private static final String FIND_ALL_QUERY =
            "SELECT * FROM genre";
    private static final String FIND_BY_ID_QUERY =
            "SELECT * FROM genre WHERE id = ?";
    private static final String FIND_FILM_GENRES_QUERY =
            "SELECT g.* " +
            "FROM genre AS g " +
            "JOIN genres AS gs ON g.id = gs.genre_id " +
            "WHERE gs.film_id = ?";
    
    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper, FIND_ALL_QUERY, FIND_BY_ID_QUERY, FIND_FILM_GENRES_QUERY);
    }
}
