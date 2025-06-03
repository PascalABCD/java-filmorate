package ru.yandex.practicum.filmorate.storageTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.friends.FriendsDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.likes.FilmLikesDbStorage;
import ru.yandex.practicum.filmorate.storage.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.mapper.MpaRatingRowMapper;
import ru.yandex.practicum.filmorate.storage.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.storage.mpa_rating.MpaRatingDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, FilmRowMapper.class,
        GenreDbStorage.class, GenreRowMapper.class,
        MpaRatingDbStorage.class, MpaRatingRowMapper.class,
        FilmLikesDbStorage.class,
        UserDbStorage.class, UserRowMapper.class,
        FriendsDbStorage.class})
public class FilmTest {
    private final FilmDbStorage filmDbStorage;

    private final Film film1 = new Film(1, "name1", "description1", LocalDate.now(), 60, new MpaRating(1));

    @BeforeEach
    void setup() {
        film1.addGenre(new Genre(1, "Комедия"));

    }

    @Test
    void testCreateFilm() {
        setup();
        filmDbStorage.create(film1);
        Film createdFilm = filmDbStorage.getById(film1.getId());
        assert createdFilm != null;
        assert createdFilm.getName().equals(film1.getName());
        assert createdFilm.getDescription().equals(film1.getDescription());
        assert createdFilm.getReleaseDate().equals(film1.getReleaseDate());
        assert createdFilm.getDuration() == film1.getDuration();
        assert createdFilm.getMpa().getId() == film1.getMpa().getId();
        assert createdFilm.getGenres().containsAll(film1.getGenres());
        System.out.println(createdFilm.getGenres());
        System.out.println(film1.getGenres());
    }
}
