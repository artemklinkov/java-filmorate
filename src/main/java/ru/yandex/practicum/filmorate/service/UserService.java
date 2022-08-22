package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        log.debug("Добавление пользователя {}", user);
        return userStorage.create(user);
    }

    public User update(User user) {
        log.debug("Обновление пользователя {}", user);
        return userStorage.update(user);
    }

    public Collection<User> getAllUsers() {
        log.debug("Получение всех пользователей");
        return userStorage.getAllUsers();
    }

    public void delete(int id) {
        userStorage.delete(id);
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public Collection<User> getFriends(int id) {
        return getUserById(id).getFriends().stream()
                .map(friendId -> getUserById(friendId))
                .toList();
    }

    public void addFriend(int userId, int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    public void deleteFriend(int userId, int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        user.deleteFriend(friendId);
        friend.deleteFriend(userId);
    }

    public Collection<User> getCommonFriends(int userId, int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        Set<Integer> mutualFriends = new HashSet<>(user.getFriends());
        mutualFriends.retainAll(friend.getFriends());

        return mutualFriends.stream()
                .map(id -> getUserById(id))
                .toList();
    }
}
