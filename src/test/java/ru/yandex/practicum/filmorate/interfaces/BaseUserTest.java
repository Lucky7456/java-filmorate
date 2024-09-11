package ru.yandex.practicum.filmorate.interfaces;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class BaseUserTest {
    protected final User user = new User();

    @BeforeEach
    void setUp() {
        user.setName("userName");
        user.setEmail("valid@mail.ru");
        user.setLogin("loginSuccess");
        user.setBirthday(LocalDate.now().minusDays(1));
    }
}
