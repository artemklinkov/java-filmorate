package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User create(User user);

    User update(User user);

    void delete(int id);

    Collection<User> getAllUsers();

    User getUserById(int id);
}
