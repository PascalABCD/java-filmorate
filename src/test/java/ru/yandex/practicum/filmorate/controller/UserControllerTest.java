package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserStorage userStorage = new InMemoryUserStorage();
    UserService userService = new UserService(userStorage);
    UserController userController = new UserController(userService);

    @Test
    public void getAllTest() {
        User user = new User();
        user.setLogin("user1");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setEmail("test@test.com");

        userController.create(user);
        assertNotNull(userController.getAll(), "User list should not be null");
    }

    @Test
    public void createTest() {
        LocalDate birthday = LocalDate.of(2000, 1, 1);

        User user = new User();
        user.setLogin("user");
        user.setBirthday(birthday);
        user.setEmail("test@test.com");

        User createdUser = userController.create(user);
        assertEquals(1, createdUser.getId());
        assertEquals("user", createdUser.getLogin());
        assertEquals("user", createdUser.getName());
        assertEquals("test@test.com", createdUser.getEmail());
        assertEquals(birthday, createdUser.getBirthday());
    }

    @Test
    public void updateTest() {
        LocalDate birthday = LocalDate.of(2000, 1, 1);

        User user = new User();
        user.setLogin("user1");
        user.setBirthday(birthday);
        user.setEmail("test@test.com");

        User createdUser = userController.create(user);

        createdUser.setLogin("updatedUser");

        User updatedUser = userController.update(createdUser);
        assertEquals(1, updatedUser.getId());
        assertEquals("updatedUser", updatedUser.getLogin());
    }
}