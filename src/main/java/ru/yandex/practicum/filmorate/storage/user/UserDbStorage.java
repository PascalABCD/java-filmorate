package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.BaseStorage;
import ru.yandex.practicum.filmorate.storage.mapper.UserRowMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class UserDbStorage extends BaseStorage<User> implements UserStorage {
    private final UserRowMapper userRowMapper;

    public UserDbStorage(JdbcTemplate jdbcTemplate, UserRowMapper userRowMapper) {
        super(jdbcTemplate);
        this.userRowMapper = userRowMapper;
    }

    @Override
    public User create(User user) {
        String sql = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName() == null ? user.getLogin() : user.getName());
            ps.setDate(4, java.sql.Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
        int updated = jdbc.update(
                sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        if (updated == 0) throw new NotFoundException("Пользователь не найден");
        return user;
    }

    @Override
    public List<User> getAll() {
        return findMany("SELECT * FROM users ORDER BY user_id ASC", userRowMapper);
    }

    @Override
    public User getById(long id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        User user = findOne(sql, userRowMapper, id);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return user;
    }
}