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

    public MPADbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<MPARating> getAllMPARatings() {
        String sql = "SELECT * FROM mpa_ratings";
        List<MPARating> mpaRatings = jdbcTemplate.query(sql, (rs, rowNum) -> makeMPARating(rs));
        return mpaRatings;
    }

    @Override
    public MPARating getMPARatingById(int id) {
        String sql = "SELECT * FROM mpa_ratings WHERE id=?";
        List<MPARating> mpaRatings = jdbcTemplate.query(sql, (rs, rowNum) -> makeMPARating(rs), id);

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
