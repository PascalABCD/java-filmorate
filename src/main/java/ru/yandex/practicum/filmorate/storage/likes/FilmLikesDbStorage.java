package ru.yandex.practicum.filmorate.storage.likes;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.List;

@Repository
public class FilmLikesDbStorage extends BaseStorage<Long> implements FilmLikesStorage {

    public FilmLikesDbStorage(JdbcTemplate jdbc) {
        super(jdbc);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        String sql = "MERGE INTO film_likes (film_id, user_id) VALUES (?, ?)";
        update(sql, filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        String sql = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
        update(sql, filmId, userId);
    }

    @Override
    public List<Long> getLikesById(Long filmId) {
        String sql = "SELECT user_id FROM film_likes WHERE film_id = ?";
        return findMany(sql, (rs, rowNum) -> rs.getLong("user_id"), filmId);
    }
}
