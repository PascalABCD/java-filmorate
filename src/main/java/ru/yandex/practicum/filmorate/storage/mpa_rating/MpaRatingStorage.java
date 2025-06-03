package ru.yandex.practicum.filmorate.storage.mpa_rating;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

public interface MpaRatingStorage {
    List<MpaRating> getAll();
    MpaRating getById(int id);
    MpaRating getByFilmId(long filmId);
}
