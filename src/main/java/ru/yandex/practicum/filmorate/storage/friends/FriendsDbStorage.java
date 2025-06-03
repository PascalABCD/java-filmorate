package ru.yandex.practicum.filmorate.storage.friends;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserRowMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FriendsDbStorage implements FriendsStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    @Override
    public void addFriend(long userId, long friendId) {
        validateUserIds(userId, friendId);
        checkSameUser(userId, friendId);

        String sql = """
                INSERT INTO friends (user_id, friend_id)
                VALUES (?, ?)
                """;
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        validateUserIds(userId, friendId);
        checkSameUser(userId, friendId);

        String sql = """
                DELETE FROM friends
                WHERE (user_id = ? AND friend_id = ?)
                """;
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> getFriends(long userId) {
        checkIfExists(userId);
        String sql = """
                    SELECT u.*
                    FROM friends f
                    JOIN users u ON f.friend_id = u.user_id
                    WHERE f.user_id = ?
                """;
        return jdbcTemplate.query(sql, userRowMapper, userId);
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherUserId) {
        validateUserIds(userId, otherUserId);
        String sql = """
                    SELECT u.*
                    FROM friends f1
                    JOIN friends f2 ON f1.friend_id = f2.friend_id
                    JOIN users u ON u.user_id = f1.friend_id
                    WHERE f1.user_id = ? AND f2.user_id = ?
                """;
        return jdbcTemplate.query(sql, userRowMapper, userId, otherUserId);
    }

    private void validateUserIds(long userId, long friendId) {
        checkIfExists(userId);
        checkIfExists(friendId);
        if (userId == friendId) {
            throw new IllegalArgumentException("Нельзя добавить себя в друзья.");
        }
    }

    private void checkIfExists(long userId) {
        String sql = """
                SELECT *
                FROM users
                WHERE user_id = ?
                """;
        List<User> user = jdbcTemplate.query(sql, new UserRowMapper(), userId);
        if (user.size() != 1) {
            throw new NotFoundException("Пользователь с id: " + userId + " не найден");
        }
    }

    private void checkSameUser(long userId, long friendId) {
        if (userId == friendId) {
            throw new IllegalArgumentException("Нельзя добавить себя в друзья.");
        }
    }
}
