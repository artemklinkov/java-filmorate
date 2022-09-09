package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Component("filmDBStorage")
public class FilmDbStorage implements FilmStorage {

    private final String SELECT_FILM = "SELECT f.id, " +
            "f.name, " +
            "f.description, " +
            "f.release_date, " +
            "f.duration, " +
            "f.rate, " +
            "f.mpa_rating, " +
            "mpa.name mpa_name " +
            "FROM films f LEFT JOIN mpa_ratings mpa ON f.mpa_rating=mpa.id WHERE f.id=?";
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        final String sql = "INSERT INTO films(name, description, release_date, duration, rate, mpa_rating) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, new String[]{"id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getRate());
            ps.setInt(6, film.getMpa().getId());
            return ps;
        }, keyHolder);

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                String sqlInsertGenres = "INSERT INTO FILM_GENRES(FILM_ID, GENRE_ID) VALUES (?, ?);";
                jdbcTemplate.update(sqlInsertGenres, keyHolder.getKey(), genre.getId());
            }
        }

        String sql_select = SELECT_FILM;
        List<Film> films = jdbcTemplate.query(sql_select, (rs, rowNum) -> makeFilm(rs), keyHolder.getKey());
        return films.get(0);
    }

    @Override
    public Film update(Film film) {

        final String sql = "UPDATE  films SET name=?, description=?, release_date=?, duration=?, rate=?, mpa_rating=?" +
                "WHERE id=?";

        int result = jdbcTemplate.update(sql, film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId()
        );

        if (result == 0) {
            throw new NotFoundException("Фильм с id %s не найден".formatted(film.getId()));
        }

        if (film.getGenres() != null) {
            String sqlRemoveGenres = "DELETE FROM FILM_GENRES WHERE FILM_ID=?;";
            jdbcTemplate.update(sqlRemoveGenres, film.getId());
            for (Genre genre : film.getGenres()) {
                String sqlInsertGenres = "INSERT INTO FILM_GENRES(FILM_ID, GENRE_ID) VALUES (?, ?);";
                jdbcTemplate.update(sqlInsertGenres, film.getId(), genre.getId());
            }
        }

        String sqlSelect = SELECT_FILM;
        List<Film> films = jdbcTemplate.query(sqlSelect, (rs, rowNum) -> makeFilm(rs), film.getId());
        Film filmUpd = films.get(0);
        return filmUpd;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM FILMS WHERE ID=?;";
        int result = jdbcTemplate.update(sql, id);

        if (result == 0) {
            throw new NotFoundException("Фильм с id %s не найден".formatted(id));
        }
    }

    @Override
    public Collection<Film> getAllFilms() {
        String sql = "SELECT f.id, " +
                "f.name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "f.rate, " +
                "f.mpa_rating, " +
                "mpa.name mpa_name " +
                "FROM films f LEFT JOIN mpa_ratings mpa ON f.mpa_rating=mpa.id";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
        return films;
    }

    @Override
    public Collection<Film> getPopular(int count) {
        String sql = "SELECT f.id, " +
                "f.name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "f.rate, " +
                "f.mpa_rating, " +
                "mpa.name mpa_name " +
                "FROM films f LEFT JOIN mpa_ratings mpa ON f.mpa_rating=mpa.id " +
                "ORDER BY RATE DESC LIMIT ?";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), count);
        return films;
    }

    @Override
    public Film getFilmById(int id) {
        String sqlSelect = SELECT_FILM;
        List<Film> films = jdbcTemplate.query(sqlSelect, (rs, rowNum) -> makeFilm(rs), id);

        if (films.size() == 0) {
            throw new NotFoundException("Фильм с id %s не найден".formatted(id));
        }

        return films.get(0);
    }

    @Override
    public void addLike(int filmId, int userId) {
        String sql = "UPDATE FILMS SET rate=rate+1 WHERE ID=?;";
        int result = jdbcTemplate.update(sql, filmId);

        if (result == 0) {
            throw new NotFoundException("Фильм с id %s не найден".formatted(filmId));
        }

        String sqlSetLike = "INSERT INTO LIKES(film_id, user_id) VALUES (?, ?)";

        result = jdbcTemplate.update(sqlSetLike, filmId, userId);

        if (result == 0) {
            throw new NotFoundException("Пользователь с id %s не найден".formatted(userId));
        }
    }

    @Override
    public void removeLike(int filmId, int userId) {
        String sql = "UPDATE FILMS SET rate=rate-1 WHERE ID=?;";
        int result = jdbcTemplate.update(sql, filmId);

        if (result == 0) {
            throw new NotFoundException("Фильм с id %s не найден".formatted(filmId));
        }

        String sqlSetLike = "DELETE FROM LIKES WHERE (film_id=? AND user_id=?)";

        result = jdbcTemplate.update(sqlSetLike, filmId, userId);

        if (result == 0) {
            sql = "UPDATE FILMS SET rate=rate+1 WHERE ID=?;";
            jdbcTemplate.update(sql, filmId);
            throw new NotFoundException("Пользователь с id %s не найден".formatted(userId));
        }
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        String sqlSelectGenres = "SELECT f.genre_id, g.name FROM FILM_GENRES f " +
                "LEFT JOIN GENRES g ON f.genre_id=g.id " +
                "WHERE f.film_id=? " +
                "ORDER BY f.genre_id";
        List<Genre> genres = jdbcTemplate.query(sqlSelectGenres, (rsGenre, rowNum) -> makeGenre(rsGenre),
                rs.getInt("id"));

        return Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .rate(rs.getInt("rate"))
                .mpa(new MPARating(rs.getInt("mpa_rating"), rs.getString("mpa_name")))
                .genres(new HashSet<>(genres))
                .build();
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("genre_id"),
                rs.getString("name"));
    }
}
