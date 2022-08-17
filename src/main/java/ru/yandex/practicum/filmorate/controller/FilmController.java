package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Добавление фильма {}", film);
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.debug("Обновление фильма {}", film);
        return filmService.update(film);
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.debug("Получение всех фильмов");
        return filmService.getAllFilms();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        log.debug("Удаление фильма");
        filmService.delete(id);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        return filmService.getFilmById(id);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable int filmId, @PathVariable int userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable int filmId, @PathVariable int userId) {
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        return getAllFilms().stream()
                .sorted(Comparator.comparing(Film::getRate).reversed())
                .limit(count)
                .collect(Collectors.toCollection(() -> new ArrayList<>()));
    }

}
