package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.likes.FilmLikesStorage;
import ru.yandex.practicum.filmorate.storage.mpa_rating.MpaRatingStorage;

import java.util.List;
import java.util.Set;

@Slf4j
@Validated
@Service
public class FilmService {
    @Autowired
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;

    @Autowired
    private final FilmLikesStorage likesStorage;
    @Autowired
    private final GenreStorage genreStorage;
    @Autowired
    private final MpaRatingStorage ratingStorage;

    public FilmService(FilmStorage filmStorage, FilmLikesStorage likesStorage, GenreStorage genreStorage, MpaRatingStorage ratingStorage) {
        this.filmStorage = filmStorage;
        this.likesStorage = likesStorage;
        this.genreStorage = genreStorage;
        this.ratingStorage = ratingStorage;
    }


    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getFilmById(Long filmId) {
        return filmStorage.getById(filmId);
    }

    public Film create(Film film) {
        MpaRating mpaRating = ratingStorage.getById(film.getMpa().getId());
        film.setMpa(mpaRating);
        List<Genre> genres = genreStorage.getByFilmId(film.getId());
        film.getGenres().forEach(genre -> {
            Genre existingGenre = genreStorage.getById(genre.getId());
            if (existingGenre != null) {
                genre.setName(existingGenre.getName());
            } else {
                throw new NotFoundException("Жанр с id " + genre.getId() + " не найден");
            }
        });
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        if (filmStorage.getById(film.getId()) == null) {
            throw new NotFoundException("фильм с id " + film.getId() + " не найден");
        }

        MpaRating mpaRating = ratingStorage.getById(film.getMpa().getId());
        film.setMpa(mpaRating);
        Set<Genre> genres = film.getGenres();
        film.setGenres(genres);
        return filmStorage.update(film);
    }

    public void addLike(Long filmId, Long userId) {
        likesStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        likesStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopular(count);
    }
}
