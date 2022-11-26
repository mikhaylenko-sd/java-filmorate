package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {
    private final FilmController filmController = new FilmController();
    private Film film1;
    private Film film2;
    private Film film3;
    private Film film4;

    @BeforeEach
    void createFilms() {
        film1 = new Film(0, "Аватар1", "Фильм про синих людей, часть 1", LocalDate.of(2009, Month.DECEMBER, 17), (long) 60);
        film2 = new Film(0, "Аватар2", "Фильм про синих людей, часть 2", LocalDate.of(2021, Month.NOVEMBER, 16), (long) 120);
        film3 = new Film(0, "Аватар3", "Фильм про синих людей, часть 3", LocalDate.of(2022, Month.NOVEMBER, 1), (long) 180);
        film4 = new Film(0, "Аватар3", "Фильм про синих людей, часть 3", LocalDate.of(2022, Month.NOVEMBER, 1), (long) 240);
    }

    //корректная работа GET
    @Test
    void getAll() {
        filmController.create(film1);
        filmController.create(film2);
        filmController.create(film3);

        assertEquals(3, filmController.getAll().size());

        assertTrue(filmController.getAll().contains(film1));
        assertTrue(filmController.getAll().contains(film2));
        assertTrue(filmController.getAll().contains(film3));
    }

    //корректная работа POST
    @Test
    void create() {
        filmController.create(film1);
        filmController.create(film2);
        filmController.create(film3);

        assertEquals(3, filmController.getAll().size());
    }

    //некорректная работа POST (условия для валидации)
    @Test
    void createInvalidFilms() {
        film1.setName("    ");
        assertThrows(ValidationException.class, () -> filmController.create(film1));

        film2.setDescription("Фильм про синих людей, часть 1. Фильм про синих людей, часть 1. Фильм про синих людей, часть 1. Фильм про синих людей, часть 1. " +
                "Фильм про синих людей, часть 1. Фильм про синих людей, часть 1. Фильм про синих людей, часть 1. Фильм про синих людей, часть 1.");
        assertThrows(ValidationException.class, () -> filmController.create(film2));

        film3.setReleaseDate(LocalDate.of(1895, Month.DECEMBER, 27));
        assertThrows(ValidationException.class, () -> filmController.create(film3));

        film4.setDuration((long) -60);
        assertThrows(ValidationException.class, () -> filmController.create(film4));
    }

    //некорректная работа POST (null)
    @Test
    void createInvalidFilmsWithNullFields() {
        film1.setName(null);
        assertThrows(ValidationException.class, () -> filmController.create(film1));

        film2.setDescription(null);
        assertThrows(ValidationException.class, () -> filmController.create(film2));

        film3.setReleaseDate(null);
        assertThrows(ValidationException.class, () -> filmController.create(film3));

        film4.setDuration(null);
        assertThrows(ValidationException.class, () -> filmController.create(film4));
    }

    //корректная работа PUT
    @Test
    void update() {
        filmController.create(film1);
        filmController.create(film2);
        filmController.create(film3);

        assertEquals(3, filmController.getAll().size());

        film2.setName("Аватар 2222");
        filmController.update(film2);
        assertTrue(filmController.getAll().contains(film2));
        assertEquals("Аватар 2222", filmController.getAll().get(1).getName());
    }

    //некорректная работа PUT (условия для валидации)
    @Test
    void updateInvalidFilms() {
        filmController.create(film1);
        filmController.create(film2);
        filmController.create(film3);
        filmController.create(film4);

        assertEquals(4, filmController.getAll().size());
        film1.setName("    ");
        assertThrows(ValidationException.class, () -> filmController.update(film1));

        film2.setDescription("Фильм про синих людей, часть 1. Фильм про синих людей, часть 1. Фильм про синих людей, часть 1. Фильм про синих людей, часть 1. " +
                "Фильм про синих людей, часть 1. Фильм про синих людей, часть 1. Фильм про синих людей, часть 1. Фильм про синих людей, часть 1. ");
        assertThrows(ValidationException.class, () -> filmController.update(film2));

        film3.setReleaseDate(LocalDate.of(1895, Month.DECEMBER, 27));
        assertThrows(ValidationException.class, () -> filmController.update(film3));

        film4.setDuration((long) -100);
        assertThrows(ValidationException.class, () -> filmController.update(film4));
    }

    //некорректная работа PUT (null)
    @Test
    void updateInvalidFilmsWithNullFields() {
        filmController.create(film1);
        filmController.create(film2);
        filmController.create(film3);
        filmController.create(film4);

        assertEquals(4, filmController.getAll().size());
        film1.setName(null);
        assertThrows(ValidationException.class, () -> filmController.update(film1));

        film2.setDescription(null);
        assertThrows(ValidationException.class, () -> filmController.update(film2));

        film3.setReleaseDate(null);
        assertThrows(ValidationException.class, () -> filmController.update(film3));

        film4.setDuration(null);
        assertThrows(ValidationException.class, () -> filmController.update(film4));
    }
}