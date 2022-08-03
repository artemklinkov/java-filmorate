package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.annotation.NoWhitespaces;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@EqualsAndHashCode
public class User {
    @EqualsAndHashCode.Exclude
    private int id;

    @Email
    @EqualsAndHashCode.Exclude
    private String email;

    @NotBlank
    @NoWhitespaces
    private String login;

    @EqualsAndHashCode.Exclude
    private String name;

    @PastOrPresent
    @EqualsAndHashCode.Exclude
    private LocalDate birthday;

    public void setName(String name) {
        this.name = name.isBlank() ? this.login : name;
    }
}
