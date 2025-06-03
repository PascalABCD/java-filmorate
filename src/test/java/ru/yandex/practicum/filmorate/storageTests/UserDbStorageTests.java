package ru.yandex.practicum.filmorate.storageTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.mapper.UserRowMapper;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({UserDbStorage.class, UserRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTests {
    private final UserDbStorage userStorage;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private User user;

    @BeforeEach
    void setup() {
        jdbcTemplate.update("DELETE FROM friends");
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1");

        user = new User();
        user.setEmail("user@example.com");
        user.setLogin("userLogin");
        user.setName("User Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));
    }

    @Test
    void testCreateUser() {
        User created = userStorage.create(user);
        assertNotNull(created.getId());
        assertEquals(user.getEmail(), created.getEmail());
        assertEquals(user.getLogin(), created.getLogin());
        assertEquals(user.getName(), created.getName());
        assertEquals(user.getBirthday(), created.getBirthday());
    }

    @Test
    void testUpdateUser() {
        User created = userStorage.create(user);
        created.setName("Updated Name");
        created.setLogin("updatedLogin");

        User updated = userStorage.update(created);
        assertEquals("Updated Name", updated.getName());
        assertEquals("updatedLogin", updated.getLogin());
    }

    @Test
    void testGetAllUsers() {
        userStorage.create(user);
        User anotherUser = new User();
        anotherUser.setEmail("other@example.com");
        anotherUser.setLogin("otherLogin");
        anotherUser.setName("Other User");
        anotherUser.setBirthday(LocalDate.of(1985, 5, 5));
        userStorage.create(anotherUser);

        List<User> users = userStorage.getAll();
        assertTrue(users.size() >= 2);
    }
}
