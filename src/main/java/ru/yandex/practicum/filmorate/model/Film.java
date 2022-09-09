package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.AfterFirstFilmReleaseDate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
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

    private int rate;

    private Set<Long> likes = new HashSet<>();

    @NotNull
    private MPARating mpa;

    private Set<Genre> genres = new HashSet<>();
    ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public void addLike(Long userId) {
        this.likes.add(userId);
    }

    public void removeLike(Long userId) {
        if (likes.contains(userId)) {
            this.likes.remove(userId);
        } else {
            throw new NotFoundException("Пользователь с id %s не ставил лайк фильму".formatted(userId));
        }
    }
}
