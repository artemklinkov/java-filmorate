package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Generator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private final Generator generator = new Generator();

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Добавление фильма {}", film);
        film.setId(generator.getNextID());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.debug("Обновление фильма {}", film);
        films.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.debug("Поллучение всех фильмов");
        return films.values();
    }

}
