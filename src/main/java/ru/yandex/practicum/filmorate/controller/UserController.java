package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private Map<Long, User> users = new HashMap<>();
    private Long id = 1L;

    @GetMapping
    public List<User> getAll() {
        log.info("Получаем всех юзеров");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Начинаем создание юзера: {}", user);
        user.setId(id++);

        // если нет имени, используем логин
        user.setDefaultName();

        users.put(user.getId(), user);
        log.info("Юзер создан: {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Начинаем обновлять данные по юзеру: {}", user);
        Long id = user.getId();

        if (!users.containsKey(id)) {
            log.error("Юзер {} не найден", user);
            throw new NotFoundException("Юзен не найден");
        }

        user.setDefaultName();
        users.put(id, user);
        log.info("Данные юзера обновлены: {}", user);
        return user;
    }
}
