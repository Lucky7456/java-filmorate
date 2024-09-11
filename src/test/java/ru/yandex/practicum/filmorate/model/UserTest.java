package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.interfaces.BaseUserTest;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserTest extends BaseUserTest {
    @Autowired
    private Validator validator;

    @Test
    void shouldFailEmptyUser() {
        User emptyUser = new User();
        Set<ConstraintViolation<User>> violations = validator.validate(emptyUser);
        assertFalse(violations.isEmpty());
        assertEquals(3, violations.size());
    }

    @Test
    void shouldFailNullOrWithWhiteSpacesLogin() {
        user.setLogin(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        user.setLogin("");
        violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        user.setLogin("login fail");
        violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailBirthdayInTheFuture() {
        user.setBirthday(LocalDate.now().plusDays(1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    void shouldSucceedCreatingUserWithEmptyNameUsingLogin() {
        user.setName(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
        assertEquals(user.getName(), user.getLogin());

        user.setName("");
        violations = validator.validate(user);
        assertTrue(violations.isEmpty());
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    void shouldFailEmptyOrInvalidEmail() {
        user.setEmail(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        user.setEmail("invalid.mail.ru");
        violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    void shouldSucceedCreatingValidUser() {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }
}
