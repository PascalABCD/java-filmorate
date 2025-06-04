package ru.yandex.practicum.filmorate.storageTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.mapper.MpaRatingRowMapper;
import ru.yandex.practicum.filmorate.storage.mpa_rating.MpaRatingDbStorage;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({MpaRatingDbStorage.class, MpaRatingRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaRatingDbStorageTests {
    private final MpaRatingDbStorage storage;

    @Test
    void testGetById() {
        MpaRating rating = storage.getById(1);
        assertEquals(1, rating.getId());
        assertNotNull(rating.getName());
    }

    @Test
    void testGetAll() {
        List<MpaRating> ratings = storage.getAll();
        assertFalse(ratings.isEmpty());
    }
}

