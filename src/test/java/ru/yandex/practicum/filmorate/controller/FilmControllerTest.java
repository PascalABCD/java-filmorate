package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmStorage filmStorage = new InMemoryFilmStorage();
    FilmService filmService = new FilmService(filmStorage);
    FilmController filmController = new FilmController(filmService);
    LocalDate releaseDate = LocalDate.of(2023, 1, 1);

    @Test
    @DisplayName("Create Film - Positive Test")
    public void createPositiveTest() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setReleaseDate(releaseDate);
        film.setDuration(120);

        Film createdFilm = filmController.create(film);
        assertEquals(1, createdFilm.getId());
        assertEquals("Test Film", createdFilm.getName());
        assertEquals("Test Description", createdFilm.getDescription());
        assertEquals(releaseDate, createdFilm.getReleaseDate());
        assertEquals(120, createdFilm.getDuration());
    }

    @Test
    @DisplayName("Update Film - Positive Test")
    public void updatePositiveTest() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setReleaseDate(releaseDate);
        film.setDuration(200);

        Film createdFilm = filmController.create(film);
        createdFilm.setName("Updated Test Film");
        createdFilm.setDescription("Updated Test Description");

        Film updatedFilm = filmController.update(createdFilm);
        assertEquals("Updated Test Film", updatedFilm.getName(), "Film name should be updated");
        assertEquals("Updated Test Description", updatedFilm.getDescription(), "Film description should be updated");
    }
}