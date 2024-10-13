package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.storage.interfaces.RatingMpaStorage;
import ru.yandex.practicum.filmorate.storage.mapper.RatingMpaRowMapper;

@JdbcTest
@Import({RatingMpaDbStorage.class, RatingMpaRowMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RatingMpaStorageTest {
    private final RatingMpaStorage storage;

    @Test
    public void testFindAllMpaRatings() {
        Assertions.assertThat(storage.findAll()).hasSize(5);
    }

    @Test
    public void testFindMpaRatingById() {
        long id = 1L;

        Assertions.assertThat(storage.findOneById(id))
                .isPresent()
                .hasValueSatisfying(
                        ratingMpa -> Assertions.assertThat(ratingMpa)
                                .hasFieldOrPropertyWithValue("id", id)
                );
    }
}