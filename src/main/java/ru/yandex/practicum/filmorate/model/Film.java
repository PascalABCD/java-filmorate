package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import java.time.LocalDate;

@Data
public class Film {
    Long id;

    @NotBlank(message = "Заполните название фильма")
    String name;

    @Size(max = 200, message = "Максимальная длина описания - 200 символов")
    String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Дата релиза не может быть в будущем")
    @ReleaseDate
    LocalDate releaseDate;

    @Positive
    int duration;
}
