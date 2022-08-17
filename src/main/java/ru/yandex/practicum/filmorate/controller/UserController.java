package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("Добавление пользователя {}", user);
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.debug("Обновление пользователя {}", user);
        return userService.update(user);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.debug("Получение всех пользователей");
        return userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        userService.delete(id);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable int id) {
        return userService.getFriends(id);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable int userId, @PathVariable int friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable int userId, @PathVariable int friendId) {
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    public Collection<User> getCommonFriends(@PathVariable int userId, @PathVariable int friendId) {
        return userService.getCommonFriends(userId, friendId);
    }
}
