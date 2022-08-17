package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void delete(int id) {
        userStorage.delete(id);
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public Collection<User> getFriends(int id) {
        User user = getUserById(id);
        return user.getFriends().stream()
                .map(friendId -> getUserById(friendId))
                .collect(Collectors.toCollection(() -> new ArrayList<>()));
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
                .collect(Collectors.toCollection(() -> new ArrayList<>()));
    }
}
