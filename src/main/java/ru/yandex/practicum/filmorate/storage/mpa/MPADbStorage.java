package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Component
public class MPADbStorage implements MPAStorage {
    private final JdbcTemplate jdbcTemplate;
    private final String SQL_SELECT_RATINGS = "SELECT * FROM mpa_ratings";
    public MPADbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<MPARating> getAllMPARatings() {
        return jdbcTemplate.query(SQL_SELECT_RATINGS, (rs, rowNum) -> makeMPARating(rs));
    }

    @Override
    public MPARating getMPARatingById(int id) {
        List<MPARating> mpaRatings = jdbcTemplate.query(SQL_SELECT_RATINGS + " WHERE id=?", (rs, rowNum) -> makeMPARating(rs), id);

        if (mpaRatings.size() == 0) {
            throw new NotFoundException("Рейтинг с id %s не найден".formatted(id));
        }

        return mpaRatings.get(0);
    }

    private MPARating makeMPARating(ResultSet rs) throws SQLException {
        return new MPARating(rs.getInt("id"),
                rs.getString("name"));
    }
}
