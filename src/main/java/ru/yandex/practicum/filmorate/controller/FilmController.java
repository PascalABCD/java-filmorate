package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private Long id = 1L;

    @GetMapping
    public List<Film> getAll() {
        log.info("Получаем все фильмы");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Создаем фильм: {}", film);
        film.setId(id++);
        films.put(film.getId(), film);

        log.info("Фильм создан: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Обновляем фильм: {}", film);
        Long id = film.getId();

        if (!films.containsKey(id)) {
            log.error("Фильм {} не найден", film);
            throw new NotFoundException("Фильм не найден");
        }

        films.put(id, film);
        log.info("Фильм обновлен: {}", film);
        return film;
    }
}
