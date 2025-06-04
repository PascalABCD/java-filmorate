package ru.yandex.practicum.filmorate.storageTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.friends.FriendsDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.likes.FilmLikesDbStorage;
import ru.yandex.practicum.filmorate.storage.mapper.*;
import ru.yandex.practicum.filmorate.storage.mpa_rating.MpaRatingDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, FilmRowMapper.class,
        GenreDbStorage.class, GenreRowMapper.class,
        MpaRatingDbStorage.class, MpaRatingRowMapper.class,
        FilmLikesDbStorage.class,
        UserDbStorage.class, UserRowMapper.class,
        FriendsDbStorage.class})
public class FilmDbStorageTests {
    private final FilmDbStorage filmDbStorage;

    private Film film;

    @BeforeEach
    void setup() {
        film = new Film(0, "Test Film", "Test description",
                LocalDate.of(2000, 1, 1), 120, new MpaRating(1));
        film.addGenre(new Genre(1, "Комедия"));
    }

    @Test
    void testCreateAndGetById() {
        Film created = filmDbStorage.create(film);
        assertNotNull(created);
        assertTrue(created.getId() > 0);

        Film result = filmDbStorage.getById(created.getId());
        assertEquals(film.getName(), result.getName());
        assertEquals(film.getDescription(), result.getDescription());
        assertEquals(film.getDuration(), result.getDuration());
        assertEquals(film.getReleaseDate(), result.getReleaseDate());
        assertEquals(film.getMpa().getId(), result.getMpa().getId());
        assertTrue(result.getGenres().containsAll(film.getGenres()));
    }

    @Test
    void testUpdateFilm() {
        Film created = filmDbStorage.create(film);
        created.setName("Updated name");
        created.setDescription("Updated description");
        created.setDuration(90);
        created.setReleaseDate(LocalDate.of(2010, 10, 10));
        created.setMpa(new MpaRating(2));
        created.setGenres(Set.of(new Genre(2, "Драма")));

        Film updated = filmDbStorage.update(created);
        assertEquals("Updated name", updated.getName());
        assertEquals("Updated description", updated.getDescription());
        assertEquals(90, updated.getDuration());
        assertEquals(LocalDate.of(2010, 10, 10), updated.getReleaseDate());
        assertEquals(2, updated.getMpa().getId());
        assertTrue(updated.getGenres().contains(new Genre(2, "Драма")));
    }

    @Test
    void testGetAllFilms() {
        filmDbStorage.create(film);
        Film film2 = new Film(0, "Film 2", "Another description",
                LocalDate.of(2001, 2, 2), 100, new MpaRating(1));
        film2.addGenre(new Genre(1, "Комедия"));
        filmDbStorage.create(film2);

        List<Film> films = filmDbStorage.getAll();
        assertTrue(films.size() >= 2);
    }
}
