package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id;

    @Email(message = "Email не корректен")
    @NotNull(message = "Поле email не может быть пустым")
    private String email;

    @NotBlank(message = "Поле логин не может быть пустым")
    @Pattern(regexp = "\\S+", message = "Логин не должен содержать пробелы")
    private String login;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Поле для даты рождения не может быть пустым")
    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    private Set<Long> friends = new HashSet<>();

    public void setDefaultName() {
        if (name == null || name.isBlank()) {
            name = login;
        }
    }
}