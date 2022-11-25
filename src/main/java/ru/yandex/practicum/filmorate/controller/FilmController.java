package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.GeneratorId;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final HashMap<Integer, Film> films = new HashMap<>();
    private final GeneratorId generatorId = new GeneratorId();


    @GetMapping("/films")
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) {
        log.debug("Получен запрос POST /film.");
        filmValidate(film);
        film.setId(generatorId.generate());
        if (!films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new IllegalArgumentException("Вы пытаетесь создать существующий Film");
        }
        return film;
    }

    @PutMapping(value = "/films")
    public Film update(@RequestBody Film film) {
        log.debug("Получен запрос PUT /film.");
        filmValidate(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new IllegalArgumentException("Вы пытаетесь обновить несуществующий Film.");
        }
        return film;
    }

    private void filmValidate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.debug("Валидация не пройдена (name).");
            throw new ValidationException("Некорректный ввод имени.");
        }
        if (film.getDescription() == null) {
            log.debug("Валидация не пройдена (description).");
            throw new ValidationException("Передано null-значение");
        } else if (film.getDescription().length() > 200) {
            log.debug("Валидация не пройдена (description).");
            throw new ValidationException("Длина описания не может превышать 200 символов.");
        }
        if (film.getReleaseDate() == null) {
            log.debug("Валидация не пройдена (releaseDate).");
            throw new ValidationException("Передано null-значение");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.debug("Валидация не пройдена (releaseDate).");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() == null) {
            log.debug("Валидация не пройдена (duration).");
            throw new ValidationException("Передано null-значение");
        } else if (film.getDuration() <= 0) {
            log.debug("Валидация не пройдена (duration).");
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }
}

