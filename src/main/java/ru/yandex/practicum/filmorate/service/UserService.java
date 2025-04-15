package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Objects;

@Slf4j
@Validated
@Service
public class UserService {

    private final UserStorage storage;

    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public List<User> getAll() {
        return storage.getAll();
    }

    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Имя не указано при создании, устанавливаем имя по умолчанию - логин");
            user.setName(user.getLogin());
        }
        log.info("Пользователь {} создан", user);
        return storage.create(user);
    }

    public User update(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Имя не указано при обновлении, устанавливаем имя по умолчанию - логин");
            user.setName(user.getLogin());
        }
        log.info("Пользователь {} обновлен", user);
        return storage.update(user);
    }

    public User addFriend(Long userId, Long friendId) {
        User user = storage.getAll().stream()
                .filter(us -> Objects.equals(us.getId(), userId))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Пользователь не найден");
                    return new NotFoundException("Пользователь не найден");
                });
        User friend = storage.getAll().stream()
                .filter(us -> Objects.equals(us.getId(), friendId))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Пользователь-друг не найден");
                    return new NotFoundException("Пользователь-друг не найден");
                });

        if (user.getFriends().contains(friendId)) {
            log.error("Пользователь уже в друзьях");
            throw new ConditionsNotMetException("Пользователь уже в друзьях");
        }

        log.info("Пользователь {} добавлен в друзья к пользователю {}", friendId, userId);
        user.getFriends().add(friendId);
        log.info("Пользователь {} добавлен в друзья к пользователю {}", userId, friendId);
        friend.getFriends().add(userId);
        log.info("Процесс добавления завершен");
        return user;
    }

    public User removeFriend(Long userId, Long friendId) {
        User user = storage.getAll().stream()
                .filter(us -> Objects.equals(us.getId(), userId))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Пользователь не найден");
                    return new NotFoundException("Пользователь не найден");
                });
        User friend = storage.getAll().stream()
                .filter(us -> Objects.equals(us.getId(), friendId))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Пользователь не найден");
                    return new NotFoundException ("Пользователь-друг не найден");
                });

        log.info("Пользователь {} удален из друзей к пользователю {}", friendId, userId);
        user.getFriends().remove(friendId);
        log.info("Пользователь {} удален из друзей к пользователю {}", userId, friendId);
        friend.getFriends().remove(userId);
        log.info("Процесс удаления завершен");
        return user;
    }

    public List<User> getFriends(Long userId) {
        User user = storage.getAll().stream()
                .filter(us -> Objects.equals(us.getId(), userId))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Пользователь не найден");
                    return  new NotFoundException("Пользователь не найден");
                });
        log.info("Пользователь {} найден", userId);
        return storage.getAll().stream()
                .filter(us -> user.getFriends().contains(us.getId()))
                .toList();
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        User user = storage.getAll().stream()
                .filter(us -> Objects.equals(us.getId(), userId))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Пользователь не найден");
                    return  new NotFoundException("Пользователь не найден");
                });
        User other = storage.getAll().stream()
                .filter(us -> Objects.equals(us.getId(), otherId))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Пользователь не найден");
                    return new NotFoundException("Пользователь-друг не найден");
                });

        log.info("Выводим список общих друзей пользователей {} и {}", userId, otherId);
        return storage.getAll().stream()
                .filter(us -> user.getFriends().contains(us.getId()) && other.getFriends().contains(us.getId()))
                .toList();
    }
}
