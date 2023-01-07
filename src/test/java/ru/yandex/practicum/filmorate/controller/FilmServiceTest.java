package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmServiceTest {
    private final FilmService filmService;
    private Film film1;
    private Film film2;
    private Film film3;
    private Film film4;

    @Autowired
    public FilmServiceTest(FilmService filmService) {
        this.filmService = filmService;
    }

    @BeforeEach
    void createFilms() {
        film1 = new Film(0, "Аватар1", "Фильм про синих людей, часть 1", LocalDate.of(2009, Month.DECEMBER, 17), (long) 60, new MPA());
        film2 = new Film(0, "Аватар2", "Фильм про синих людей, часть 2", LocalDate.of(2021, Month.NOVEMBER, 16), (long) 120, new MPA());
        film3 = new Film(0, "Аватар3", "Фильм про синих людей, часть 3", LocalDate.of(2022, Month.NOVEMBER, 1), (long) 180, new MPA());
        film4 = new Film(0, "Аватар3", "Фильм про синих людей, часть 3", LocalDate.of(2022, Month.NOVEMBER, 1), (long) 240, new MPA());
    }

    //корректная работа GET
    @Test
    void getAll() {
        int oldSize = filmService.getAll().size();
        filmService.create(film1);
        filmService.create(film2);
        filmService.create(film3);

        assertEquals(oldSize + 3, filmService.getAll().size());

        assertTrue(filmService.getAll().contains(film1));
        assertTrue(filmService.getAll().contains(film2));
        assertTrue(filmService.getAll().contains(film3));
    }

    //корректная работа POST
    @Test
    void create() {
        filmService.create(film1);
        filmService.create(film2);
        filmService.create(film3);

        assertEquals(3, filmService.getAll().size());
    }

    //некорректная работа POST (условия для валидации)
    @Test
    void createInvalidFilms() {
        film1.setName("    ");
        assertThrows(ValidationException.class, () -> filmService.create(film1));

        film2.setDescription("Фильм про синих людей, часть 1. Фильм про синих людей, часть 1. Фильм про синих людей, часть 1. Фильм про синих людей, часть 1. " +
                "Фильм про синих людей, часть 1. Фильм про синих людей, часть 1. Фильм про синих людей, часть 1. Фильм про синих людей, часть 1.");
        assertThrows(ValidationException.class, () -> filmService.create(film2));

        film3.setReleaseDate(LocalDate.of(1895, Month.DECEMBER, 27));
        assertThrows(ValidationException.class, () -> filmService.create(film3));

        film4.setDuration((long) -60);
        assertThrows(ValidationException.class, () -> filmService.create(film4));
    }

    //некорректная работа POST (null)
    @Test
    void createInvalidFilmsWithNullFields() {
        film1.setName(null);
        assertThrows(ValidationException.class, () -> filmService.create(film1));

        film2.setDescription(null);
        assertThrows(ValidationException.class, () -> filmService.create(film2));

        film3.setReleaseDate(null);
        assertThrows(ValidationException.class, () -> filmService.create(film3));

        film4.setDuration(null);
        assertThrows(ValidationException.class, () -> filmService.create(film4));
    }

    //корректная работа PUT
    @Test
    void update() {
        filmService.create(film1);
        filmService.create(film2);
        filmService.create(film3);

        film2.setName("Аватар 2222");
        filmService.update(film2);
        assertTrue(filmService.getAll().contains(film2));
        assertEquals("Аватар 2222", filmService.getById(film2.getId()).getName());
    }

    //некорректная работа PUT (условия для валидации)
    @Test
    void updateInvalidFilms() {
        filmService.create(film1);
        filmService.create(film2);
        filmService.create(film3);
        filmService.create(film4);

        film1.setName("    ");
        assertThrows(ValidationException.class, () -> filmService.update(film1));

        film2.setDescription("Фильм про синих людей, часть 1. Фильм про синих людей, часть 1. Фильм про синих людей, часть 1. Фильм про синих людей, часть 1. " +
                "Фильм про синих людей, часть 1. Фильм про синих людей, часть 1. Фильм про синих людей, часть 1. Фильм про синих людей, часть 1. ");
        assertThrows(ValidationException.class, () -> filmService.update(film2));

        film3.setReleaseDate(LocalDate.of(1895, Month.DECEMBER, 27));
        assertThrows(ValidationException.class, () -> filmService.update(film3));

        film4.setDuration((long) -100);
        assertThrows(ValidationException.class, () -> filmService.update(film4));
    }

    //некорректная работа PUT (null)
    @Test
    void updateInvalidFilmsWithNullFields() {
        filmService.create(film1);
        filmService.create(film2);
        filmService.create(film3);
        filmService.create(film4);

        film1.setName(null);
        assertThrows(ValidationException.class, () -> filmService.update(film1));

        film2.setDescription(null);
        assertThrows(ValidationException.class, () -> filmService.update(film2));

        film3.setReleaseDate(null);
        assertThrows(ValidationException.class, () -> filmService.update(film3));

        film4.setDuration(null);
        assertThrows(ValidationException.class, () -> filmService.update(film4));
    }
}