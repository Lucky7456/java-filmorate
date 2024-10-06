package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.interfaces.RatingMpaStorage;
import ru.yandex.practicum.filmorate.storage.mappers.RatingMpaRowMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({RatingMpaDbStorage.class, RatingMpaRowMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RatingMpaStorageTest {
    private final RatingMpaStorage storage;
    
    @Test
    public void testFindAllMpaRatings() {
        assertThat(storage.findAll()).hasSize(5);
    }
    
    @Test
    public void testFindMpaRatingById() {
        int id = 1;
        Optional<RatingMpa> ratingMpaOptional = storage.findOneById(id);
        
        assertThat(ratingMpaOptional)
                .isPresent()
                .hasValueSatisfying(
                        ratingMpa -> assertThat(ratingMpa)
                                .hasFieldOrPropertyWithValue("id", id)
                );
    }
}
