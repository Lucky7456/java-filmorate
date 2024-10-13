package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendsStorage;

@JdbcTest
@Import(FriendsDbStorage.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FriendsStorageTest {
    private final FriendsStorage storage;

    @Test
    public void testShouldAddFriendToUser() {
        long userId = 3L;
        long friendId = 1L;

        Assertions.assertThat(storage.insert(userId, friendId)).isEqualTo(1);
    }

    @Test
    public void testShouldRemoveFriendFromUser() {
        long userId = 1L;
        long friendId = 4L;

        Assertions.assertThat(storage.delete(userId, friendId)).isEqualTo(1);
    }
}