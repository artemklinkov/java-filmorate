package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.annotation.AfterFirstFilmReleaseDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;


public class AfterFirstFilmReleaseDateValidator implements ConstraintValidator<AfterFirstFilmReleaseDate, LocalDate> {
    private final static LocalDate FIRST_FILM_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value.isAfter(FIRST_FILM_RELEASE_DATE);
    }

}