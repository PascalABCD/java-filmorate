package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Validated
@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User user) {
        checkName(user);
        log.info("Пользователь {} создан", user);
        return userStorage.create(user);
    }

    public User update(User user) {
        getUserById(user.getId());
        checkName(user);
        log.info("Пользователь {} обновлен", user);
        return userStorage.update(user);
    }

    public User addFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

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
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        log.info("Пользователь {} удален из друзей к пользователю {}", friendId, userId);
        user.getFriends().remove(friendId);
        log.info("Пользователь {} удален из друзей к пользователю {}", userId, friendId);
        friend.getFriends().remove(userId);
        log.info("Процесс удаления завершен");
        return user;
    }

    public List<User> getFriends(Long userId) {
        log.info("Пользователь {} найден", userId);
        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        log.info("Выводим список общих друзей пользователей {} и {}", userId, otherId);
        return userStorage.getCommonFriends(userId, otherId);
    }

    private User getUserById(long id) {
        return userStorage.getById(id);
    }

    private void checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Имя не указано при создании, устанавливаем имя по умолчанию - логин");
            user.setName(user.getLogin());
        }
    }
}
