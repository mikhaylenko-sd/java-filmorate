package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmDbStorageTest {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private Film film1;
    private Film film2;
    private User user;

    @BeforeEach
    void setUp() {
        film1 = filmStorage.create(new Film(0, "Аватар1", "Фильм про синих людей, часть 1", LocalDate.of(2009, Month.DECEMBER, 17), (long) 60, new MPA(1, null, null)));
        film2 = filmStorage.create(new Film(0, "Аватар2", "Фильм про синих людей, часть 2", LocalDate.of(2021, Month.NOVEMBER, 16), (long) 120, new MPA(2, null, null)));
        user = userStorage.create(new User(0, "user111@mail.ru", "login 1", "User 1", LocalDate.of(2001, Month.AUGUST, 1)));
    }

    @Test
    void getAll() {
        List<Film> films = filmStorage.getAll();
        assertEquals(2, films.size());
    }

    @Test
    void getById() {
        Film film = filmStorage.getById(film1.getId());
        assertEquals(film1.getId(), film.getId());
        assertEquals(film1.getName(), film.getName());
        assertEquals(film1.getDescription(), film.getDescription());
        assertEquals(film1.getReleaseDate(), film.getReleaseDate());
        assertEquals(film1.getDuration(), film.getDuration());

        film = filmStorage.getById(film2.getId());
        assertEquals(film2.getId(), film.getId());
        assertEquals(film2.getName(), film.getName());
        assertEquals(film2.getDescription(), film.getDescription());
        assertEquals(film2.getReleaseDate(), film.getReleaseDate());
        assertEquals(film2.getDuration(), film.getDuration());
    }

    @Test
    void create() {
        assertEquals(2, filmStorage.getAll().size());
        Film film3 = new Film(0, "Аватар3", "Фильм про синих людей, часть 3", LocalDate.of(2022, Month.NOVEMBER, 1), (long) 180, new MPA(3, null, null));
        filmStorage.create(film3);

        assertEquals(3, filmStorage.getAll().size());
    }

    @Test
    void update() {
        film1.setName("11Film11");
        film1.setReleaseDate(LocalDate.of(2000, Month.APRIL, 3));
        filmStorage.update(film1);

        Film film = filmStorage.getById(film1.getId());
        assertEquals("11Film11", film.getName());
        assertEquals(LocalDate.of(2000, Month.APRIL, 3), film.getReleaseDate());

        assertEquals(new HashSet<>(), film.getGenres());
        Genre genre = new Genre(1, "Комедия");
        film1.getGenres().add(genre);
        filmStorage.update(film1);

        film = filmStorage.getById(film1.getId());
        assertEquals(Set.of(genre), film.getGenres());

        int userId = user.getId();
        assertEquals(new HashSet<>(), film1.getLikes());
        filmStorage.addLike(film1.getId(), userId);
        filmStorage.update(film1);

        film = filmStorage.getById(film1.getId());
        assertEquals(Set.of(userId), film.getLikes());
    }

    @Test
    void contains() {
        assertTrue(filmStorage.contains(1));
        assertTrue(filmStorage.contains(2));

        assertFalse(filmStorage.contains(3));
    }

    @Test
    void addLike() {
        int id = film1.getId();
        int userId = user.getId();
        assertEquals(new HashSet<>(), filmStorage.getById(id).getLikes());

        filmStorage.addLike(id, userId);

        assertEquals(Set.of(userId), filmStorage.getById(id).getLikes());
    }

    @Test
    void removeLike() {
        int id = film1.getId();
        int userId = user.getId();

        filmStorage.addLike(id, userId);
        assertEquals(Set.of(userId), filmStorage.getById(id).getLikes());

        filmStorage.removeLike(id, userId);

        assertEquals(new HashSet<>(), filmStorage.getById(id).getLikes());
    }

}
