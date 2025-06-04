package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private long id;

    @NotBlank(message = "Заполните название фильма")
    private String name;

    @Size(max = 200, message = "Максимальная длина описания - 200 символов")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Дата релиза не может быть в будущем")
    @ReleaseDate
    private LocalDate releaseDate;

    @Positive
    private int duration;

    Set<Genre> genres = new HashSet<>();

    private MpaRating mpa;

    private Set<Long> likes = new HashSet<>();

    public Film(long id, String name, String description, LocalDate releaseDate, int duration, MpaRating mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }
}
