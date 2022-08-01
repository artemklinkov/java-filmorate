package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.annotation.NoWhitespaces;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class NoWhitespacesValidator implements ConstraintValidator<NoWhitespaces, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && !value.contains(" ");
    }

}