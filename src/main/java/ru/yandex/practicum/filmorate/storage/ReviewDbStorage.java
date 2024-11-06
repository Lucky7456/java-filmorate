package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.interfaces.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.util.BaseCrudStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ReviewDbStorage extends BaseCrudStorage<Review> implements ReviewStorage {
    private static final String TABLE_NAME = "reviews";
    private static final String FIND_ONE_BY_ID_QUERY =
            "SELECT r.*, COUNT(l.user_id) - COUNT(d.user_id) AS useful " +
            "FROM reviews AS r " +
            "LEFT JOIN reviews_likes AS l ON l.review_id = r.id " +
            "LEFT JOIN reviews_dislikes AS d ON d.review_id = r.id " +
            "WHERE r.id = ? " +
            "GROUP BY r.id ";
    private static final String FIND_BY_FILM_AND_COUNT_QUERY =
            "SELECT r.*, COUNT(l.user_id) - COUNT(d.user_id) AS useful " +
            "FROM reviews AS r " +
            "LEFT JOIN reviews_likes AS l ON l.review_id = r.id " +
            "LEFT JOIN reviews_dislikes AS d ON d.review_id = r.id " +
            "%s" +
            "GROUP BY r.id " +
            "ORDER BY useful DESC " +
            "LIMIT ?";
    private static final String UPDATE_QUERY =
            "UPDATE reviews " +
            "SET content = ?, is_positive = ? " +
            "WHERE id = ?";

    public ReviewDbStorage(JdbcTemplate jdbc, RowMapper<Review> mapper) {
        super(jdbc, mapper, TABLE_NAME, FIND_BY_FILM_AND_COUNT_QUERY, UPDATE_QUERY);
    }

    @Override
    public Optional<Review> findOneById(long id) {
        return findOne(FIND_ONE_BY_ID_QUERY, id);
    }

    @Override
    public List<Review> findAllBy(long filmId, long count) {
        Object[] params;
        StringBuilder sb = new StringBuilder();
        if (filmId == 0) {
            params = new Object[]{count};
        } else {
            sb.append("WHERE r.film_id = ? ");
            params = new Object[]{filmId, count};
        }
        return findMany(String.format(FIND_BY_FILM_AND_COUNT_QUERY, sb), params);
    }

    @Override
    protected Map<String, Object> toMap(Review entity) {
        Map<String, Object> values = new HashMap<>();
        values.put("content", entity.getContent());
        values.put("is_positive", entity.getIsPositive());
        values.put("user_id", entity.getUserId());
        values.put("film_id", entity.getFilmId());
        return values;
    }
}
