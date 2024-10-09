package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({UserDbStorage.class, UserRowMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserStorageTest {
    private final UserStorage storage;

    @Test
    public void testFindAllUsers() {
        assertThat(storage.findAll()).hasSize(5);
    }

    @Test
    public void testFindUserById() {
        long id = 1L;
        Optional<User> userOptional = storage.findOneById(id);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(
                        user -> assertThat(user)
                                .hasFieldOrPropertyWithValue("id", id)
                );
    }

    @Test
    public void testFindAllFriendsOfUser() {
        long id = 1L;

        assertThat(storage.findAllBy(id))
                .hasOnlyElementsOfType(User.class)
                .hasSize(4);
    }

    @Test
    public void testPersistCreatesEntityUser() {
        User user = new User();
        user.setName("userName");
        user.setEmail("valid@mail.ru");
        user.setLogin("loginSuccess");
        user.setBirthday(LocalDate.now().minusDays(1));

        long id = storage.create(user);

        user.setId(id);

        assertThat(storage.findOneById(id))
                .isPresent()
                .hasValue(user);
    }

    @Test
    public void testShouldUpdateUserSuccessfully() {
        long id = 1L;

        User user = storage.findOneById(id)
                .orElseThrow(() -> new NotFoundException("user not found"));

        user.setLogin("otherLogin");

        storage.update(
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId()
        );

        assertThat(storage.findOneById(id)).hasValue(user);
    }

    @Test
    public void testShouldDeleteUserSuccessfully() {
        long id = 1L;
        storage.delete(id);

        assertThat(storage.findOneById(id)).isEmpty();
    }

    @Test
    public void testFindMutualFriendsOfUserWithAnotherUser() {
        long userId = 1L;
        long otherId = 2L;

        assertThat(storage.findAllMutualFriends(userId, otherId))
                .hasOnlyElementsOfType(User.class)
                .hasSize(3);
    }

    @Test
    public void testShouldAddFriendToUser() {
        long userId = 4L;
        long friendId = 1L;

        assertThat(storage.addFriend(userId, friendId)).isEqualTo(1);
    }

    @Test
    public void testShouldRemoveFriendFromUser() {
        long userId = 1L;
        long friendId = 4L;

        assertThat(storage.removeFriend(userId, friendId)).isEqualTo(1);
    }
}