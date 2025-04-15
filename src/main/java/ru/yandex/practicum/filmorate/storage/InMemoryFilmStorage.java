package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private Long id = 1L;

    @Override
    public Film create(Film film) {
        film.setId(id++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAll() {
        return List.copyOf(films.values());
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм не найден");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getById(long id) {
        Film film = films.get(id);
        if (film == null) {
            throw new NotFoundException("Фильм не найден");
        }
        return film;
    }

    @Override
    public List<Film> getPopular(int count) {
        return films.values().stream()
                .sorted(Comparator.comparingInt(f -> -f.getLikes().size()))
                .limit(count)
                .toList();
    }

}
