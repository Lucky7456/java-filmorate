package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.storage.interfaces.LikesStorage;

@JdbcTest
@Import(LikesDbStorage.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LikesStorageTest {
    private final LikesStorage storage;

    @Test
    public void testShouldAddLikeToFilmFromUser() {
        long filmId = 4L;
        long userId = 1L;

        Assertions.assertThat(storage.insert(filmId, userId)).isEqualTo(1);
    }

    @Test
    public void testShouldRemoveLikeToFilmFromUser() {
        long filmId = 1L;
        long userId = 1L;

        Assertions.assertThat(storage.delete(filmId, userId)).isEqualTo(1);
    }
}