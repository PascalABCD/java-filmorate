package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MpaRatingRowMapper implements RowMapper<MpaRating> {
    @Override
    public MpaRating mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MpaRating(
                rs.getInt("mpa_rating_id"), rs.getString("mpa_rating_name")
        );
    }
}

