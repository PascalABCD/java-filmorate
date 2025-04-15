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
import java.util.Objects;

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
        User user = userStorage.getAll().stream()
                .filter(user1 -> Objects.equals(user1.getId(), userId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Film film = filmStorage.getAll().stream()
                .filter(film1 -> Objects.equals(film1.getId(), filmId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));

        if (film.getLikes().contains(userId)) {
            log.error("Юзер {} уже поставил лайк", userId);
            throw new NotFoundException("Пользователь уже поставил лайк");
        }
        film.getLikes().add(user);
        log.info("Юзер {} поставил лайк фильму {}", userId, filmId);
        return film;
    }

    public Film removeLike(Long filmId, Long userId) {
        User user = userStorage.getAll().stream()
                .filter(user1 -> Objects.equals(user1.getId(), userId))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Юзер {} не найден", userId);
                    return new NotFoundException("Пользователь не найден");
                });

        Film film = filmStorage.getAll().stream()
                .filter(film1 -> Objects.equals(film1.getId(), filmId))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Фильм {} не найден", filmId);
                    return new NotFoundException("Фильм не найден");
                });

        if (!film.getLikes().contains(user)) {
            log.error("Юзер {} не ставил лайк", userId);
            throw new NotFoundException("Пользователь не ставил лайк");
        }
        film.getLikes().remove(user);
        log.info("Юзер {} убрал лайк у фильма {}", userId, filmId);
        return film;
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getAll().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .toList();
    }
}
