package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Создаем юзера {}", user);
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Обновляем юзера {}", user);
        return userService.update(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public User addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Добавляем юзера {} в друзья к юзеру {}", friendId, userId);
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public User removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Удаляем юзера {} из друзей юзера {}", friendId, userId);
        return userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@PathVariable Long userId) {
        log.info("Получаем список друзей юзера {}", userId);
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long userId, @PathVariable Long otherId) {
        log.info("Получаем список общих друзей юзеров {} и {}", userId, otherId);
        return userService.getCommonFriends(userId, otherId);
    }
}
