package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Generator;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private final Generator generator = new Generator();

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("Добавление пользователя {}", user);
        if (users.get(user.getId()) == null) {
            user.setId(generator.getNextID());
            users.put(user.getId(), user);
        } else {
            user.setId(users.get(user.getLogin()).getId());
            users.put(user.getId(), user);
        }
        return users.get(user.getId());
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.debug("Обновление пользователя {}", user);
        if (users.get(user.getId()) == null) {
            throw new RuntimeException("Пользователь не найден");
        }

        users.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.debug("Поллучение всех пользователей");
        return users.values();
    }

}
