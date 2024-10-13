package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.interfaces.BaseUserTest;

import java.time.LocalDate;
import java.util.Set;

public class UserTest extends BaseUserTest {
    @Test
    void shouldFailEmptyUser() {
        User emptyUser = new User();
        Set<ConstraintViolation<User>> violations = validator.validate(emptyUser);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(3, violations.size());
    }

    @Test
    void shouldFailNullOrWithWhiteSpacesLogin() {
        user.setLogin(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());

        user.setLogin("");
        violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());

        user.setLogin("login fail");
        violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
    }

    @Test
    void shouldFailBirthdayInTheFuture() {
        user.setBirthday(LocalDate.now().plusDays(1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
    }

    @Test
    void shouldSucceedCreatingUserWithEmptyNameUsingLogin() {
        user.setName(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertTrue(violations.isEmpty());
        Assertions.assertEquals(user.getName(), user.getLogin());

        user.setName("");
        violations = validator.validate(user);
        Assertions.assertTrue(violations.isEmpty());
        Assertions.assertEquals(user.getName(), user.getLogin());
    }

    @Test
    void shouldFailEmptyOrInvalidEmail() {
        user.setEmail(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());

        user.setEmail("invalid.mail.ru");
        violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
    }

    @Test
    void shouldSucceedCreatingValidUser() {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertTrue(violations.isEmpty());
    }
}