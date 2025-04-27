package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film addLike(Long filmId, Long userId) {
        Film film = getFilmById(filmId);
        getUserById(userId);

        if (film.getLikes().contains(userId)) {
            log.error("Юзер {} уже поставил лайк", userId);
            throw new NotFoundException("Пользователь уже поставил лайк");
        }
        film.getLikes().add(userId);
        log.info("Юзер {} поставил лайк фильму {}", userId, filmId);
        return film;
    }

    public Film removeLike(Long filmId, Long userId) {
        Film film = getFilmById(filmId);

        if (!film.getLikes().contains(userId)) {
            log.error("Юзер {} не ставил лайк", userId);
            throw new NotFoundException("Пользователь не ставил лайк");
        }
        film.getLikes().remove(userId);
        log.info("Юзер {} убрал лайк у фильма {}", userId, filmId);
        return film;
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopular(count);
    }

    private Film getFilmById(Long filmId) {
        return filmStorage.getById(filmId);
    }

    private User getUserById(Long userId) {
        return userStorage.getById(userId);
    }
}
