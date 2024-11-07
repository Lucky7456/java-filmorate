package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;
import ru.yandex.practicum.filmorate.storage.interfaces.*;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final ReviewLikesStorage likesStorage;
    private final ReviewDislikesStorage dislikesStorage;
    private final FeedStorage feedStorage;
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    public List<Review> findAllBy(long filmId, long count) {
        return reviewStorage.findAllBy(filmId, count);
    }

    public Review getReviewById(long id) {
        log.debug("getReviewById {}", id);
        return reviewStorage.findOneById(id).orElseThrow();
    }

    public Review create(Review review) {
        log.debug("create review {}", review);
        filmStorage.findOneById(review.getFilmId()).orElseThrow();
        userStorage.findOneById(review.getUserId()).orElseThrow();
        review.setReviewId(reviewStorage.create(review));
        feedStorage.create(new Feed(null, review.getUserId(), EventType.REVIEW, Operation.ADD, review.getReviewId(), Instant.now().toEpochMilli()));
        return review;
    }

    public Review update(Review review) {
        log.debug("update review {}", review);
        int updatedRows = reviewStorage.update(
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId()
        );
        if (updatedRows == 1) {
            Review updatedReview = reviewStorage.findOneById(review.getReviewId()).orElseThrow();
            feedStorage.create(new Feed(null, updatedReview.getUserId(), EventType.REVIEW, Operation.UPDATE, updatedReview.getReviewId(), Instant.now().toEpochMilli()));
            return updatedReview;
        }
        throw new NoSuchElementException("review not found");
    }

    public void delete(long id) {
        log.debug("delete review {}", id);
        Review review = reviewStorage.findOneById(id).orElseThrow();
        feedStorage.create(new Feed(null, review.getUserId(), EventType.REVIEW, Operation.REMOVE, id, Instant.now().toEpochMilli()));
        reviewStorage.delete(id);
    }

    public void addLike(long reviewId, long userId) {
        log.debug("{} addLike {}", reviewId, userId);
        likesStorage.insert(reviewId, userId);
        if (dislikesStorage.exists(reviewId, userId) != 0) {
            removeDislike(reviewId, userId);
        }
    }

    public void removeLike(long reviewId, long userId) {
        log.debug("{} removeLike {}", reviewId, userId);
        likesStorage.delete(reviewId, userId);
    }

    public void addDislike(long reviewId, long userId) {
        log.debug("{} addDislike {}", reviewId, userId);
        dislikesStorage.insert(reviewId, userId);
        if (likesStorage.exists(reviewId, userId) != 0) {
            removeLike(reviewId, userId);
        }
    }

    public void removeDislike(long reviewId, long userId) {
        log.debug("{} removeDislike {}", reviewId, userId);
        dislikesStorage.delete(reviewId, userId);
    }
}
