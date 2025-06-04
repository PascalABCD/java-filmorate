package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.mpa_rating.MpaRatingDbStorage;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FilmDbStorage extends BaseStorage<Film> implements FilmStorage {
    private final MpaRatingDbStorage mpaDb;
    private final GenreDbStorage genreDb;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbc,
                         MpaRatingDbStorage mpaDb,
                         GenreDbStorage genreDb) {
        super(jdbc);
        this.mpaDb = mpaDb;
        this.genreDb = genreDb;
    }

    @Override
    public Film create(Film film) {
        film.setMpa(mpaDb.getById(film.getMpa().getId()));
        film.getGenres().forEach(
                genre -> genre.setName(genreDb.getNameById(genre.getId()))
        );

        long id = insert(
                "INSERT INTO films (name, description, release_date, duration, mpa_rating_id) VALUES (?, ?, ?, ?, ?)",
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId()
        );

        film.setId(id);
        String insertGenresQuery = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
        for (Genre genre : film.getGenres()) {
            jdbc.update(insertGenresQuery, film.getId(), genre.getId());
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        film.setMpa(mpaDb.getById(film.getMpa().getId()));
        String sql = "UPDATE films SET name=?, description=?, release_date=?, duration=?, mpa_rating_id=? WHERE film_id=?";
        update(sql,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        return film;
    }

    @Override
    public List<Film> getAll() {
        String sql = """
                SELECT f.*, m.*
                FROM films f
                LEFT JOIN mpa_ratings AS m ON f.mpa_rating_id = m.mpa_rating_id
                ORDER BY f.film_id
                """;
        List<Film> films = jdbc.query(sql, new FilmRowMapper());
        loadGenres(films);
        loadLikesForFilms(films);
        return films;
    }

    @Override
    public Film getById(long id) {
        String sql = """
                SELECT f.*, m.*
                FROM films f
                LEFT JOIN mpa_ratings AS m ON f.mpa_rating_id = m.mpa_rating_id
                WHERE f.film_id = ?
                """;
        Film film = findOne(sql, new FilmRowMapper(), id);
        if (film == null) throw new NotFoundException("Фильм с id " + id + " не найден");

        loadGenres(List.of(film));
        loadLikesForFilms(List.of(film));
        return film;
    }

    @Override
    public List<Film> getPopular(int count) {
        String sql = """
                SELECT f.*, m.mpa_rating_id, m.mpa_rating_name
                            FROM films f
                            LEFT JOIN film_likes fl ON f.film_id = fl.film_id
                            JOIN mpa_ratings m ON f.mpa_rating_id = m.mpa_rating_id
                            GROUP BY f.film_id
                            ORDER BY COUNT(fl.user_id) DESC
                            LIMIT ?
                """;
        List<Film> films = jdbc.query(sql, new FilmRowMapper(), count);
        loadGenres(films);
        loadLikesForFilms(films);
        return films;
    }

    private void loadGenres(List<Film> films) {
        if (films.isEmpty()) return;

        List<Long> filmIds = films.stream()
                .map(Film::getId)
                .toList();

        String sql = String.format("""
                SELECT fg.film_id, g.genre_id, g.genre_name
                FROM film_genres fg
                JOIN genres g ON fg.genre_id = g.genre_id
                WHERE fg.film_id IN (%s)
                ORDER BY g.genre_id
                """, filmIds.stream().map(String::valueOf).collect(Collectors.joining(",")));

        Map<Long, Set<Genre>> genresByFilmId = new HashMap<>();

        jdbc.query(sql, rs -> {
            long filmId = rs.getLong("film_id");
            Genre genre = new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
            genresByFilmId.computeIfAbsent(filmId, k -> new LinkedHashSet<>()).add(genre);
        });

        for (Film film : films) {
            Set<Genre> genres = genresByFilmId.getOrDefault(film.getId(), new LinkedHashSet<>());
            film.setGenres(genres);
        }
    }

    private void loadLikesForFilms(List<Film> films) {
        if (films.isEmpty()) return;

        List<Long> filmIds = films.stream().map(Film::getId).toList();

        String sql = String.format("SELECT film_id, user_id FROM film_likes WHERE film_id IN (%s)",
                filmIds.stream().map(String::valueOf).collect(Collectors.joining(",")));

        Map<Long, Set<Long>> likesMap = new HashMap<>();

        jdbc.query(sql, rs -> {
            long filmId = rs.getLong("film_id");
            long userId = rs.getLong("user_id");
            likesMap.computeIfAbsent(filmId, k -> new HashSet<>()).add(userId);
        });

        for (Film film : films) {
            Set<Long> likes = likesMap.getOrDefault(film.getId(), new HashSet<>());
            film.setLikes(likes);
        }
    }
}


