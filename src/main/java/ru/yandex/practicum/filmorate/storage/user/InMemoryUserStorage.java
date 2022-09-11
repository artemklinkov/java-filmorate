package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Generator;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component("userInMemoryStorage")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private final Generator generator = new Generator();

    @Override
    public User create(User user) {
        if (users.get(user.getId()) == null) {
            user.setId(generator.getNextID());
            users.put(user.getId(), user);
        } else {
            user.setId(users.get(user.getLogin()).getId());
            users.put(user.getId(), user);
        }
        return users.get(user.getId());
    }

    @Override
    public User update(User user) {
        if (users.get(user.getId()) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public void delete(int id) {
        users.remove(id);
    }

    @Override
    public User getUserById(int id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с id %s не найден".formatted(id));
        }

        return user;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        user.deleteFriend(friendId);
        friend.deleteFriend(userId);
    }

    @Override
    public void confirmFriendship(int userId, int friendId) {
        return;
    }

    @Override
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
