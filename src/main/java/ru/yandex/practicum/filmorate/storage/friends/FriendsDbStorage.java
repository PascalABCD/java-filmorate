package ru.yandex.practicum.filmorate.storage.friends;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
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
        String sql = """
                INSERT INTO friends (user_id, friend_id)
                VALUES (?, ?)
                """;
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        String sql = """
                DELETE FROM friends
                WHERE (user_id = ? AND friend_id = ?)
                """;
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> getFriends(long userId) {
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
        String sql = """
                    SELECT u.*
                    FROM friends f1
                    JOIN friends f2 ON f1.friend_id = f2.friend_id
                    JOIN users u ON u.user_id = f1.friend_id
                    WHERE f1.user_id = ? AND f2.user_id = ?
                """;
        return jdbcTemplate.query(sql, userRowMapper, userId, otherUserId);
    }
}
