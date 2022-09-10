package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Component
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private final String SQL_SELECT_GENRES = "SELECT * FROM genres";
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Genre> getAllGenres() {
        return jdbcTemplate.query(SQL_SELECT_GENRES, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Genre getGenreById(int id) {
        List<Genre> genres = jdbcTemplate.query(SQL_SELECT_GENRES + " WHERE id=?", (rs, rowNum) -> makeGenre(rs), id);

        if (genres.size() == 0) {
            throw new NotFoundException("Жанр с id %s не найден".formatted(id));
        }

        return genres.get(0);
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("id"),
                rs.getString("name"));
    }
}
