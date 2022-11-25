package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;


class UserControllerTest {
    private final UserController userController = new UserController();

    //корректная работа GET
    @Test
    void getAll() {
        User user1 = new User(0, "user111@mail.ru", "login 1", "User 1", LocalDate.of(2001, Month.AUGUST, 1));
        User user2 = new User(0, "user222@mail.ru", "login 2", "User 2", LocalDate.of(2002, Month.SEPTEMBER, 2));
        User user3 = new User(0, "user333@mail.ru", "login 3", "User 3", LocalDate.of(2003, Month.APRIL, 3));

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
        User user1 = new User(0, "user111@mail.ru", "login 1", "User 1", LocalDate.of(2001, Month.AUGUST, 1));
        User user2 = new User(0, "user222@mail.ru", "login 2", "User 2", LocalDate.of(2002, Month.SEPTEMBER, 2));
        User user3 = new User(0, "user333@mail.ru", "login 3", "User 3", LocalDate.of(2003, Month.APRIL, 3));

        userController.create(user1);
        userController.create(user2);
        userController.create(user3);

        assertEquals(3, userController.getAll().size());
    }

    //некорректная работа POST (условия для валидации)
    @Test
    void createInvalidUsers() {
        User user = new User(0, "     ", "login 1", "User 1", LocalDate.of(2001, Month.AUGUST, 1));
        assertThrows(ValidationException.class, () -> userController.create(user));
        User user1 = new User(0, "user111mail.ru", "login 1", "User 1", LocalDate.of(2001, Month.AUGUST, 1));
        assertThrows(ValidationException.class, () -> userController.create(user1));

        User user2 = new User(0, "user111@mail.ru", "", "User 1", LocalDate.of(2001, Month.AUGUST, 1));
        assertThrows(ValidationException.class, () -> userController.create(user2));
        User user3 = new User(0, "user111@mail.ru", "    ", "User 1", LocalDate.of(2001, Month.AUGUST, 1));
        assertThrows(ValidationException.class, () -> userController.create(user3));

        User user4 = new User(0, "user111@mail.ru", "login 1", "", LocalDate.of(2001, Month.AUGUST, 1));
        userController.create(user4);
        assertEquals(user4.getLogin(), userController.getAll().get(0).getName());

        User user5 = new User(0, "user111@mail.ru", "login 1", "User 1", LocalDate.of(2023, Month.AUGUST, 1));
        assertThrows(ValidationException.class, () -> userController.create(user5));
    }

    //некорректная работа POST (null)
    @Test
    void createInvalidUsersWithNullFields() {
        User user1 = new User(0, null, "login 1", "User 1", LocalDate.of(2001, Month.AUGUST, 1));
        assertThrows(ValidationException.class, () -> userController.create(user1));

        User user2 = new User(0, "user111@mail.ru", null, "User 1", LocalDate.of(2001, Month.AUGUST, 1));
        assertThrows(ValidationException.class, () -> userController.create(user2));

        User user3 = new User(0, "user111@mail.ru", "login 1", null, LocalDate.of(2001, Month.AUGUST, 1));
        userController.create(user3);
        assertEquals(user3.getLogin(), userController.getAll().get(0).getName());

        User user4 = new User(0, "user111@mail.ru", "login 1", "User 1", null);
        assertThrows(ValidationException.class, () -> userController.create(user4));
    }


    //корректная работа PUT
    @Test
    void update() {
        User user1 = new User(0, "user111@mail.ru", "login 1", "User 1", LocalDate.of(2001, Month.AUGUST, 1));
        User user2 = new User(0, "user222@mail.ru", "login 2", "User 2", LocalDate.of(2002, Month.SEPTEMBER, 2));
        User user3 = new User(0, "user333@mail.ru", "login 3", "User 3", LocalDate.of(2003, Month.APRIL, 3));

        userController.create(user1);
        userController.create(user2);
        userController.create(user3);

        assertEquals(3, userController.getAll().size());
        user2.setName("User22222222");
        userController.update(user2);
        int id2 = user2.getId();
        assertTrue(userController.getAll().contains(user2));
        assertEquals("User22222222", userController.getAll().get(1).getName());
    }

    //некорректная работа PUT (условия для валидации)
    @Test
    void updateInvalidUsers() {
        User user1 = new User(0, "user111@mail.ru", "login 1", "User 1", LocalDate.of(2001, Month.AUGUST, 1));
        User user2 = new User(0, "user222@mail.ru", "login 2", "User 2", LocalDate.of(2002, Month.SEPTEMBER, 2));
        User user3 = new User(0, "user333@mail.ru", "login 3", "User 3", LocalDate.of(2003, Month.APRIL, 3));
        User user4 = new User(0, "user111@mail.ru", "login 1", "User 1", LocalDate.of(2001, Month.AUGUST, 1));
        User user5 = new User(0, "user222@mail.ru", "login 5", "User 5", LocalDate.of(2002, Month.SEPTEMBER, 2));
        User user6 = new User(0, "user333@mail.ru", "login 3", "User 3", LocalDate.of(2003, Month.APRIL, 3));

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
        User user1 = new User(0, "user111@mail.ru", "login 1", "User 1", LocalDate.of(2001, Month.AUGUST, 1));
        User user2 = new User(0, "user222@mail.ru", "login 2", "User 2", LocalDate.of(2002, Month.SEPTEMBER, 2));
        User user3 = new User(0, "user333@mail.ru", "login 3", "User 3", LocalDate.of(2003, Month.APRIL, 3));
        User user4 = new User(0, "user444@mail.ru", "login 4", "User 4", LocalDate.of(2004, Month.AUGUST, 1));

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