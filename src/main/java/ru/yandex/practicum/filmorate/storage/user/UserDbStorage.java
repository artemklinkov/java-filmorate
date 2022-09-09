package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Component("userDBStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        final String sql = "INSERT INTO users(email, login, name, birthday) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, new String[]{"id"});
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));

            return ps;
        }, keyHolder);

        String sql_select = "SELECT * FROM users WHERE id=?";
        List<User> users = jdbcTemplate.query(sql_select, (rs, rowNum) -> makeUser(rs), keyHolder.getKey());
        return users.get(0);
    }

    @Override
    public User update(User user) {
        final String sql = "UPDATE  users SET email=?, login=?, name=?, birthday=?" +
                "WHERE id=?";

        int result = jdbcTemplate.update(sql, user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId()
        );

        if (result == 0) {
            throw new NotFoundException("Пользователь с id %s не найден".formatted(user.getId()));
        }

        if (user.getFriends() != null) {
            String sqlRemoveFriends = "DELETE FROM FRIENDS WHERE USER_ID=?;";
            jdbcTemplate.update(sqlRemoveFriends, user.getId());
            for (Integer friendId : user.getFriends()) {
                String sqlInsertGenres = "INSERT INTO FRIENDS(USER_ID, FRIEND_ID) VALUES (?, ?);";
                jdbcTemplate.update(sqlInsertGenres, user.getId(), friendId);
            }
        }

        String sqlSelect = "SELECT * FROM users WHERE id=?";
        List<User> users = jdbcTemplate.query(sqlSelect, (rs, rowNum) -> makeUser(rs), user.getId());
        User userUpd = users.get(0);

        return userUpd;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM users WHERE ID=?;";
        int result = jdbcTemplate.update(sql, id);

        if (result == 0) {
            throw new NotFoundException("Пользователь с id %s не найден".formatted(id));
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
        return users;
    }

    @Override
    public User getUserById(int id) {
        String sqlSelect = "SELECT * FROM users WHERE id=?";
        List<User> users = jdbcTemplate.query(sqlSelect, (rs, rowNum) -> makeUser(rs), id);

        if (users.size() == 0) {
            throw new NotFoundException("Пользователь с id %s не найден".formatted(id));
        }

        return users.get(0);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        int result = 0;
        try {
            String sql = "INSERT INTO friends(user_id, friend_id, is_confirmed) VALUES (?, ?, FALSE)";
            result = jdbcTemplate.update(sql, userId, friendId);
        } catch (Exception e) {
            result = 0;
        }

        if (result == 0) {
            throw new NotFoundException("Проверьте правильность указания идентификаторов");
        }
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        String sql = "DELETE FROM friends WHERE USER_ID=? AND FRIEND_ID=?";
        int result = jdbcTemplate.update(sql, userId, friendId);

        if (result == 0) {
            throw new NotFoundException("Проверьте правильность указания идентификаторов");
        }
    }

    @Override
    public void confirmFriendship(int userId, int friendId) {
        String sql = "UPDATE friends SET is_confirmed=TRUE WHERE USER_ID=? AND FRIEND_ID=?)";
        int result = jdbcTemplate.update(sql, userId, friendId);

        if (result == 0) {
            throw new NotFoundException("Проверьте правильность указания идентификаторов");
        }
    }

    @Override
    public Collection<User> getCommonFriends(int userId, int friendId) {
        String sql = "SELECT DISTINCT u.friend_id id, users.email, users.login, users.name, users.birthday " +
                "FROM friends  u " +
                "INNER JOIN (SELECT * FROM friends WHERE user_id=?) f " +
                "ON u.friend_id=f.friend_id " +
                "AND u.user_id<>f.friend_id " +
                "INNER JOIN USERS " +
                "ON u.friend_id=users.id " +
                "WHERE u.user_id=?";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), friendId, userId);
        return users;
    }

    private User makeUser(ResultSet rs) throws SQLException {
        String sqlSelectGenres = "SELECT * FROM FRIENDS WHERE USER_ID=?";
        List<Integer> friends = jdbcTemplate.query(sqlSelectGenres, (rsGenre, rowNum) -> makeFriends(rsGenre),
                rs.getInt("id"));
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());
        user.setName(rs.getString("name"));
        user.setFriends(new HashSet<>(friends));
        return user;
    }

    private Integer makeFriends(ResultSet rs) throws SQLException {
        return rs.getInt("friend_id");
    }
}
