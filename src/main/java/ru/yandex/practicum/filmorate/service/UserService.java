package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Validated
@Service
public class UserService {

    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendsStorage friendsStorage) {
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User getUserById(long id) {
        return userStorage.getById(id);
    }

    public User create(User user) {
        checkName(user);
        log.info("Пользователь {} создан", user);
        return userStorage.create(user);
    }

    public User update(User user) {
        log.info("Проверяем, что пользователь {} существует ", user);
        getUserById(user.getId());
        checkName(user);
        log.info("Пользователь {} обновлен", user);
        return userStorage.update(user);
    }

    public void addFriend(Long userId, Long friendId) {
        log.info("Проверяем, что оба пользователя существуют");
        getUserById(userId);
        getUserById(friendId);
        log.info("Пользователь {} добавляет в друзья пользователя {}", userId, friendId);
        friendsStorage.addFriend(userId, friendId);
        log.info("Пользователь {} успешно добавил в друзья пользователя {}", userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        friendsStorage.removeFriend(userId, friendId);
        log.info("Процесс удаления завершен");
    }

    public List<User> getFriends(Long userId) {
        log.info("Выводим список друзей пользователя {}", userId);
        return friendsStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        log.info("Выводим список общих друзей пользователей {} и {}", userId, otherId);
        return friendsStorage.getCommonFriends(userId, otherId);
    }

    private void checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Имя не указано при создании, устанавливаем имя по умолчанию - логин");
            user.setName(user.getLogin());
        }
    }
}
