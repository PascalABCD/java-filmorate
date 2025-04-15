package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private Map<Long, User> users = new HashMap<>();
    private Long id = 1L;

    @Override
    public List<User> getAll() {
        return List.copyOf(users.values());
    }

    @Override
    public User create(User user) {
        user.setId(id++);
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public User update(User user) {
        Long id = user.getId();
        if (!users.containsKey(id)) {
            throw new NotFoundException("Юзен не найден");
        }
        user.setDefaultName();
        users.put(id, user);
        return user;
    }

    @Override
    public User getById(long id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException("Юзер не найден");
        }
        return user;
    }


    @Override
    public List<User> getFriends(long id) {
        User user = getById(id);
        return user.getFriends().stream()
                .map(this::getById)
                .toList();
    }

    @Override
    public List<User> getCommonFriends(long id, long otherId) {
        User user = getById(id);
        User otherUser = getById(otherId);
        return user.getFriends().stream()
                .filter(otherUser.getFriends()::contains)
                .map(this::getById)
                .toList();
    }
}
