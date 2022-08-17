package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Generator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private final Generator generator = new Generator();

    @Override
    public Film create(Film film) {
        film.setId(generator.getNextID());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (films.get(film.getId()) == null) {
            throw new NotFoundException("Фильм не найден");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public void delete(int id) {
        films.remove(id);
    }

    @Override
    public Film getFilmById(int id) {
        return films.values().stream()
                .filter(film -> film.getId() == id)
                .findFirst().orElseThrow(() -> new NotFoundException(String.format("Фильм с id %s не найден", id)));
    }

    public void addLike(int filmId, int userId) {
        Film film = getFilmById(filmId);
        film.addLike((long) userId);
        film.setRate(film.getRate() + 1);
    }

    public void removeLike(int filmId, int userId) {
        Film film = getFilmById(filmId);
        film.removeLike((long) userId);
        film.setRate(film.getRate() - 1);
    }
}
