package ru.yandex.practicum.filmorate.interfaces;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class BaseUserTest {
    protected final User user = new User();
    protected Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        user.setName("userName");
        user.setEmail("valid@mail.ru");
        user.setLogin("loginSuccess");
        user.setBirthday(LocalDate.now().minusDays(1));
        user.setId(9999L);
    }
}