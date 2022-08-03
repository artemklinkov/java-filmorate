package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validateWithRightUserData() {
        User user = new User();
        user.setId(1);
        user.setEmail("test@test.ru");
        user.setLogin("NickName");
        user.setName("Nick Name");
        user.setBirthday(LocalDate.of(1988, 8, 21));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void validateWithWrongEmail() {
        User user = new User();
        user.setId(1);
        user.setEmail("test@ test.ru");
        user.setLogin("NickName");
        user.setName("Nick Name");
        user.setBirthday(LocalDate.of(1988, 8, 21));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validateWithEmptyName() {
        User user = new User();
        user.setId(1);
        user.setEmail("test@test.ru");
        user.setLogin("NickName");
        user.setName("");
        user.setBirthday(LocalDate.of(1988, 8, 21));

        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void validateWitWhitespacesInLogin() {
        User user = new User();
        user.setId(1);
        user.setEmail("test@test.ru");
        user.setLogin("Nick Name");
        user.setName("Nick Name");
        user.setBirthday(LocalDate.of(1988, 8, 21));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validateWithBirthdayInFuture() {
        User user = new User();
        user.setId(1);
        user.setEmail("test@test.ru");
        user.setLogin("NickName");
        user.setName("Nick Name");
        user.setBirthday(LocalDate.of(2024, 8, 21));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

}