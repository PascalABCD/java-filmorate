package ru.yandex.practicum.filmorate.storageTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mapper.GenreRowMapper;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({GenreDbStorage.class, GenreRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTests {
    private final GenreDbStorage storage;

    @Test
    void testGetById() {
        Genre genre = storage.getById(1);
        assertEquals(1, genre.getId());
        assertNotNull(genre.getName());
    }

    @Test
    void testGetAll() {
        List<Genre> genres = storage.getAll();
        assertFalse(genres.isEmpty());
    }

    @Test
    void testGetByFilmId() {
        List<Genre> genres = storage.getByFilmId(1);
        assertNotNull(genres);
    }
}
