package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilmTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validateWithRightFilmData() {
        Film film = Film.builder()
                .id(1)
                .name("Test name")
                .description("Test description")
                .releaseDate(LocalDate.of(2022, 8, 1))
                .duration(60)
                .mpa(new MPARating(1, "G"))
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    void validateWithEmptyFilmName() {
        Film film = Film.builder().build();
        film.setId(1);
        film.setName("");
        film.setDescription("Test description");
        film.setReleaseDate(LocalDate.of(2022, 8, 1));
        film.setDuration(60);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validateWithFilmDescriptionOver200symbols() {
        Film film = Film.builder().build();
        film.setId(1);
        film.setName("Test name");
        film.setDescription("Test description Test description Test description Test description Test description" +
                "Test description Test description Test description Test description Test description Test description" +
                "Test description Test description Test description Test description Test description");
        film.setReleaseDate(LocalDate.of(2022, 8, 1));
        film.setDuration(60);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validateWithFilmReleaseDateBefore28121895() {
        Film film = Film.builder().build();
        film.setId(1);
        film.setName("Test name");
        film.setDescription("Test description");
        film.setReleaseDate(LocalDate.of(1894, 12, 28));
        film.setDuration(60);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validateWithNegativeFilmDuration() {
        Film film = Film.builder().build();
        film.setId(1);
        film.setName("Test name");
        film.setDescription("Test description");
        film.setReleaseDate(LocalDate.of(2022, 8, 1));
        film.setDuration(-60);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

}