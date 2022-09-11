package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;

@Slf4j
@Service
public class FilmService {
    private FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("filmDBStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film create(Film film) {
        log.debug("Добавление фильма {}", film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        log.debug("Обновление фильма {}", film);
        return filmStorage.update(film);
    }

    public Collection<Film> getAllFilms() {
        log.debug("Получение всех фильмов");
        return filmStorage.getAllFilms();
    }

    public void delete(int id) {
        log.debug("Удаление фильма");
        filmStorage.delete(id);
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public void addLike(int filmId, int userId) {
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        filmStorage.removeLike(filmId, userId);
    }

    public Collection<Film> getPopular(int count) {
        return filmStorage.getPopular(count);
    }
}
