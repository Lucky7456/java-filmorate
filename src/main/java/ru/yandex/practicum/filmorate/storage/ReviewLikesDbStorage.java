package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.interfaces.ReviewLikesStorage;
import ru.yandex.practicum.filmorate.storage.util.BaseInsertDeleteStorage;

@Repository
public class ReviewLikesDbStorage extends BaseInsertDeleteStorage implements ReviewLikesStorage {
    private static final String LIKE_INSERT_QUERY =
            "INSERT INTO reviews_likes (review_id, user_id) " +
            "VALUES (?, ?)";
    private static final String LIKE_DELETE_QUERY =
            "DELETE FROM reviews_likes " +
            "WHERE review_id = ? AND user_id = ?";
    private static final String EXISTS_QUERY =
            "SELECT COUNT(*) " +
            "FROM reviews_likes " +
            "WHERE review_id = ? AND user_id = ?";

    public ReviewLikesDbStorage(JdbcTemplate jdbc) {
        super(jdbc, LIKE_INSERT_QUERY, LIKE_DELETE_QUERY);
    }

    @Override
    public int exists(long reviewId, long userId) {
        return count(EXISTS_QUERY, reviewId, userId);
    }
}
