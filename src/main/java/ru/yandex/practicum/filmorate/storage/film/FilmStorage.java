package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    void delete(int id);

    Collection<Film> getAllFilms();

    Film getFilmById(int id);

    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);
}
