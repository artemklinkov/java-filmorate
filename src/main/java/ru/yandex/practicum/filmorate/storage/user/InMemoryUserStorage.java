package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Generator;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
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
        return users.values()
                .stream()
                .filter(user -> user.getId() == id)
                .findFirst().orElseThrow(()
                        -> new NotFoundException(String.format("Пользователь с id %s не найден", id))
                );
    }
}
