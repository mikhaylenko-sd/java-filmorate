package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
public interface FilmStorage {
    List<Film> getAll();

    Film create(Film film);

    Film update(Film film);

    Film getById(int id);

    boolean contains(int id);

    void deleteAll();
    void deleteById(int id);

    void addLike(int id, int userId);

    void removeLike(int id, int userId);
}
