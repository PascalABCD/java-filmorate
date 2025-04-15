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

    public List<User> getAll() {
        return List.copyOf(users.values());
    }

    public User create(User user) {
        user.setId(id++);
        users.put(user.getId(), user);

        return user;
    }

    public User update(User user) {
        Long id = user.getId();
        if (!users.containsKey(id)) {
            throw new NotFoundException("Юзен не найден");
        }
        user.setDefaultName();
        users.put(id, user);
        return user;
    }
}
