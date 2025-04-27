package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User create(User user);

    User update(User user);

    List<User> getAll();

    User getById(long id);

    List<User> getFriends(long id);

    List<User> getCommonFriends(long id, long otherId);
}
