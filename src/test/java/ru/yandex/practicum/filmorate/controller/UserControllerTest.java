package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;


class UserControllerTest {
    private final UserController userController = new UserController();
    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private User user5;
    private User user6;

    @BeforeEach
    void createFilms() {
        user1 = new User(0, "user111@mail.ru", "login 1", "User 1", LocalDate.of(2001, Month.AUGUST, 1));
        user2 = new User(0, "user222@mail.ru", "login 2", "User 2", LocalDate.of(2002, Month.SEPTEMBER, 2));
        user3 = new User(0, "user333@mail.ru", "login 3", "User 3", LocalDate.of(2003, Month.APRIL, 3));
        user4 = new User(0, "user444@mail.ru", "login 4", "User 4", LocalDate.of(2004, Month.APRIL, 4));
        user5 = new User(0, "user555@mail.ru", "login 5", "User 5", LocalDate.of(2005, Month.APRIL, 5));
        user6 = new User(0, "user666@mail.ru", "login 6", "User 6", LocalDate.of(2006, Month.APRIL, 6));
    }

    //корректная работа GET
    @Test
    void getAll() {
        userController.create(user1);
        userController.create(user2);
        userController.create(user3);

        assertEquals(3, userController.getAll().size());

        assertTrue(userController.getAll().contains(user1));
        assertTrue(userController.getAll().contains(user2));
        assertTrue(userController.getAll().contains(user3));
    }

    //корректная работа POST
    @Test
    void create() {
        userController.create(user1);
        userController.create(user2);
        userController.create(user3);

        assertEquals(3, userController.getAll().size());
    }

    //некорректная работа POST (условия для валидации)
    @Test
    void createInvalidUsers() {
        user1.setEmail("     ");
        assertThrows(ValidationException.class, () -> userController.create(user1));
        user2.setEmail("user111mail.ru");
        assertThrows(ValidationException.class, () -> userController.create(user2));

        user3.setLogin("");
        assertThrows(ValidationException.class, () -> userController.create(user3));
        user4.setLogin("    ");
        assertThrows(ValidationException.class, () -> userController.create(user4));

        user5.setName("");
        userController.create(user5);
        assertEquals(user5.getLogin(), userController.getAll().get(0).getName());

        user6.setBirthday(LocalDate.of(2023, Month.AUGUST, 1));
        assertThrows(ValidationException.class, () -> userController.create(user6));
    }

    //некорректная работа POST (null)
    @Test
    void createInvalidUsersWithNullFields() {
        user1.setEmail(null);
        assertThrows(ValidationException.class, () -> userController.create(user1));

        user2.setLogin(null);
        assertThrows(ValidationException.class, () -> userController.create(user2));

        user3.setName(null);
        userController.create(user3);
        assertEquals(user3.getLogin(), userController.getAll().get(0).getName());

        user4.setBirthday(null);
        assertThrows(ValidationException.class, () -> userController.create(user4));
    }


    //корректная работа PUT
    @Test
    void update() {
        userController.create(user1);
        userController.create(user2);
        userController.create(user3);

        assertEquals(3, userController.getAll().size());
        user2.setName("User22222222");
        userController.update(user2);
        assertTrue(userController.getAll().contains(user2));
        assertEquals("User22222222", userController.getAll().get(1).getName());
    }

    //некорректная работа PUT (условия для валидации)
    @Test
    void updateInvalidUsers() {
        userController.create(user1);
        userController.create(user2);
        userController.create(user3);
        userController.create(user4);
        userController.create(user5);
        userController.create(user6);

        assertEquals(6, userController.getAll().size());

        user1.setEmail("    ");
        assertThrows(ValidationException.class, () -> userController.update(user1));

        user2.setEmail("user111mail.ru");
        assertThrows(ValidationException.class, () -> userController.update(user2));

        user3.setLogin("");
        assertThrows(ValidationException.class, () -> userController.update(user3));

        user4.setLogin("    ");
        assertThrows(ValidationException.class, () -> userController.update(user4));

        user5.setName("");
        userController.update(user5);
        assertEquals(user5.getLogin(), userController.getAll().get(4).getName());

        user6.setBirthday(LocalDate.of(2023, Month.AUGUST, 1));
        assertThrows(ValidationException.class, () -> userController.update(user6));
    }

    //некорректная работа PUT (null)
    @Test
    void updateInvalidUsersWithNullFields() {
        userController.create(user1);
        userController.create(user2);
        userController.create(user3);
        userController.create(user4);

        assertEquals(4, userController.getAll().size());

        user1.setEmail(null);
        assertThrows(ValidationException.class, () -> userController.update(user1));

        user2.setLogin(null);
        assertThrows(ValidationException.class, () -> userController.update(user2));

        user3.setName(null);
        userController.update(user3);
        assertEquals(user3.getLogin(), userController.getAll().get(2).getName());

        user4.setBirthday(null);
        assertThrows(ValidationException.class, () -> userController.update(user4));
    }

}