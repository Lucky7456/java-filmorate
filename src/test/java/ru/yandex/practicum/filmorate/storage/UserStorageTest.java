package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.storage.mapper.UserRowMapper;

import java.time.LocalDate;

@JdbcTest
@Import({UserDbStorage.class, UserRowMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserStorageTest {
    private final UserStorage storage;

    @Test
    public void testFindAllUsers() {
        Assertions.assertThat(storage.findAll()).hasSize(5);
    }

    @Test
    public void testFindUserById() {
        long id = 1L;

        Assertions.assertThat(storage.findOneById(id))
                .isPresent()
                .hasValueSatisfying(
                        user -> Assertions.assertThat(user)
                                .hasFieldOrPropertyWithValue("id", id)
                );
    }

    @Test
    public void testFindAllFriendsOfUser() {
        long id = 1L;

        Assertions.assertThat(storage.findAllBy(id))
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

        Assertions.assertThat(storage.findOneById(id))
                .isPresent()
                .hasValue(user);
    }

    @Test
    public void testShouldUpdateUserSuccessfully() {
        long id = 1L;

        User user = storage.findOneById(id).orElseThrow();

        user.setLogin("otherLogin");

        storage.update(
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId()
        );

        Assertions.assertThat(storage.findOneById(id)).hasValue(user);
    }

    @Test
    public void testShouldDeleteUserSuccessfully() {
        long id = 1L;
        storage.delete(id);

        Assertions.assertThat(storage.findOneById(id)).isEmpty();
    }

    @Test
    public void testFindMutualFriendsOfUserWithAnotherUser() {
        long userId = 1L;
        long otherId = 2L;

        Assertions.assertThat(storage.findAllMutualFriends(userId, otherId))
                .hasOnlyElementsOfType(User.class)
                .hasSize(3);
    }
}