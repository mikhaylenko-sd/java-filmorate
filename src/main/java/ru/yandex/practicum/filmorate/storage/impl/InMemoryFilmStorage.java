package ru.yandex.practicum.filmorate.storage.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.GeneratorId;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final Logger log = LoggerFactory.getLogger(InMemoryFilmStorage.class);
    private final Map<Integer, Film> films = new HashMap<>();
    private final GeneratorId generatorId = new GeneratorId();

    @Override
    public List<Film> getAll() {
        log.debug("Получен список фильмов");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film create(Film film) {
        film.setId(generatorId.generate());
        createIfContainsKey(film);
        log.debug("Создан фильм");
        return film;
    }

    @Override
    public Film update(Film film) {
        updateIfContainsKey(film);
        log.debug("Обновлен фильм");
        return film;
    }

    @Override
    public Film getById(int id) {
        return films.get(id);
    }

    @Override
    public boolean contains(int id) {
        return films.containsKey(id);
    }

    private void createIfContainsKey(Film film) {
        if (!films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new IllegalArgumentException("Вы пытаетесь создать существующий Film");
        }
    }

    private void updateIfContainsKey(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new IllegalArgumentException("Вы пытаетесь обновить несуществующий Film.");
        }
    }
}
