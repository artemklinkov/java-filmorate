package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.AfterFirstFilmReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {
    private final static LocalDate FIRST_FILM_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    private int id;

    @NotBlank(message = "Название не может быть пустым.")
    private String name;

    @Size(max = 200, message = "Длина описания может быть не более 200 символов.")
    private String description;

    @AfterFirstFilmReleaseDate(message = "Дата релиза не может быть ранее 28.12.1895г.")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность должна быть положительным числом.")
    private int duration;
}
