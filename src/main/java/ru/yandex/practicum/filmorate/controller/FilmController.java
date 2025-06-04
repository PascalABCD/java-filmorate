package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("Получаем все фильмы");
        return filmService.getAll();
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable long filmId) {
        log.info("Получаем фильм с id {}", filmId);
        return filmService.getFilmById(filmId);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Создаем фильм {}", film);
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Обновляем фильм {}", film);
        return filmService.update(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("Добавляем лайк фильму {} от юзера {}", filmId, userId);
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("Удаляем лайк фильма {} от юзера {}", filmId, userId);
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Получаем {} популярных фильмов", count);
        return filmService.getPopularFilms(count);
    }
}
