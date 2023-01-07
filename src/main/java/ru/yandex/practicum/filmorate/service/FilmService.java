package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.service.UserService.INVALID;
import static ru.yandex.practicum.filmorate.service.UserService.INVALID_NULL;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;

    private final UserService userService;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film create(Film film) {
        validateFilm(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        validateFilm(film);
        return filmStorage.update(film);
    }

    public Film getById(int id) {
        validateId(id);
        Film film = filmStorage.getById(id);
        if (film != null) {
            return film;
        } else {
            throw new FilmNotFoundException(id);
        }
    }

    public boolean contains(int id) {
        validateId(id);
        return filmStorage.contains(id);
    }

    public Film addLike(int id, int userId) {
        validateIds(id, userId);
        Film film = getById(id);

        areFilmAndUserEqualNull(id, userId);

        filmStorage.addLike(id, userId);
        return film;
    }

    public Film removeLike(int id, int userId) {
        validateIds(id, userId);
        Film film = getById(id);

        areFilmAndUserEqualNull(id, userId);

        filmStorage.removeLike(id, userId);
        return film;
    }

    public List<Film> getTopFilmCount(int count) {
        int numOfFilms = filmStorage.getAll().size();
        if (count <= 0) {
            throw new IncorrectParameterException("count");
        }
        if (count > numOfFilms) {
            count = numOfFilms;
        }
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparingInt(film -> -film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    private void validateId(int id) {
        if (id <= 0) {
            throw new IncorrectParameterException("id");
        }
    }

    private void validateIds(int id, int userId) {
        if (id <= 0 || userId <= 0) {
            throw new IncorrectParameterException("id");
        }
    }

    private void areFilmAndUserEqualNull(int filmId, int userId) {
        if (getById(filmId) == null) {
            throw new FilmNotFoundException(filmId);
        }
        if (!userService.contains(userId)) {
            throw new UserNotFoundException(userId);
        }
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.debug(INVALID);
            throw new ValidationException("Некорректный ввод имени.");
        }
        if (film.getDescription() == null) {
            log.debug(INVALID);
            throw new ValidationException(INVALID_NULL);
        } else if (film.getDescription().length() > 200) {
            log.debug(INVALID);
            throw new ValidationException("Длина описания не может превышать 200 символов.");
        }
        if (film.getReleaseDate() == null) {
            log.debug(INVALID);
            throw new ValidationException(INVALID_NULL);
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.debug(INVALID);
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() == null) {
            log.debug(INVALID);
            throw new ValidationException(INVALID_NULL);
        } else if (film.getDuration() <= 0) {
            log.debug(INVALID);
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }
}

