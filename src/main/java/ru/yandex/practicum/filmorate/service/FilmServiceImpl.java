package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.GeneratorId;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FilmServiceImpl implements FilmService {
    private static final Logger log = LoggerFactory.getLogger(FilmServiceImpl.class);
    private final Map<Integer, Film> films = new HashMap<>();
    private final GeneratorId generatorId = new GeneratorId();

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film create(Film film) {
        log.debug("Получен запрос POST /film.");
        validateFilm(film);
        film.setId(generatorId.generate());
        containsKeyWhenCreate(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        log.debug("Получен запрос PUT /film.");
        validateFilm(film);
        containsKeyWhenUpdate(film);
        return film;
    }

    private void validateFilm(Film film) {
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

    private void containsKeyWhenCreate(Film film) {
        if (!films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new IllegalArgumentException("Вы пытаетесь создать существующий Film");
        }
    }

    private void containsKeyWhenUpdate(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new IllegalArgumentException("Вы пытаетесь обновить несуществующий Film.");
        }
    }
}
