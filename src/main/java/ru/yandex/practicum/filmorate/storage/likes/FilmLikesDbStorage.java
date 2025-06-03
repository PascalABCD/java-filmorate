package ru.yandex.practicum.filmorate.storage.likes;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmLikesDbStorage implements FilmLikesStorage {
    private final JdbcTemplate jdbc;

    @Override
    public void addLike(Long filmId, Long userId) {
        String sql = "MERGE INTO film_likes (film_id, user_id) VALUES (?, ?)";
        jdbc.update(sql, filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        String sql = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
        jdbc.update(sql, filmId, userId);
    }

    @Override
    public List<Long> getLikesById(Long filmId) {
        String sql = "SELECT user_id FROM film_likes WHERE film_id = ?";
        return jdbc.query(sql, (rs, rowNum) -> rs.getLong("user_id"), filmId);
    }
}
