package ru.yandex.practicum.filmorate.storage.mpa_rating;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.mapper.MpaRatingRowMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MpaRatingDbStorage implements MpaRatingStorage{

    private final JdbcTemplate jdbcTemplate;
    private final MpaRatingRowMapper mpaRatingRowMapper;

    @Override
    public List<MpaRating> getAll() {
        String sql = "SELECT * FROM mpa_ratings ORDER BY mpa_rating_id";
        return jdbcTemplate.query(sql, mpaRatingRowMapper);
    }

    @Override
    public MpaRating getById(int id) {
        String sql = "SELECT * FROM mpa_ratings WHERE mpa_rating_id = ?";
        List<MpaRating> ratings = jdbcTemplate.query(sql, mpaRatingRowMapper, id);
        if (ratings.size() != 1) {
            throw new NotFoundException("Рейтинг с id: " + id + " не найден");
        }
        return ratings.getFirst();
    }

    @Override
    public MpaRating getByFilmId(long filmId) {
        String sql = "SELECT m.* FROM films f " +
                     "JOIN mpa_ratings m ON f.mpa_rating_id = m.mpa_rating_id " +
                     "WHERE f.film_id = ?";
        List<MpaRating> ratings = jdbcTemplate.query(sql, mpaRatingRowMapper, filmId);
        return ratings.getFirst();
    }
}

