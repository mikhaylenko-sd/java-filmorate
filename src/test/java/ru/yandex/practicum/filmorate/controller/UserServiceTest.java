package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceTest {
    private final UserService userService;
    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private User user5;
    private User user6;

    @Autowired
    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }

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
        int oldSize = userService.getAll().size();
        userService.create(user1);
        userService.create(user2);
        userService.create(user3);

        assertEquals(oldSize + 3, userService.getAll().size());

        assertTrue(userService.getAll().contains(user1));
        assertTrue(userService.getAll().contains(user2));
        assertTrue(userService.getAll().contains(user3));
    }

    //корректная работа POST
    @Test
    void create() {
        int oldSize = userService.getAll().size();

        userService.create(user1);
        userService.create(user2);
        userService.create(user3);

        assertEquals(oldSize + 3, userService.getAll().size());
    }

    //некорректная работа POST (условия для валидации)
    @Test
    void createInvalidUsers() {
        user1.setEmail("     ");
        assertThrows(ValidationException.class, () -> userService.create(user1));
        user2.setEmail("user111mail.ru");
        assertThrows(ValidationException.class, () -> userService.create(user2));

        user3.setLogin("");
        assertThrows(ValidationException.class, () -> userService.create(user3));
        user4.setLogin("    ");
        assertThrows(ValidationException.class, () -> userService.create(user4));

        user5.setName("");
        userService.create(user5);
        assertEquals(user5.getLogin(), userService.getAll().get(0).getName());

        user6.setBirthday(LocalDate.of(2023, Month.AUGUST, 1));
        assertThrows(ValidationException.class, () -> userService.create(user6));
    }

    //некорректная работа POST (null)
    @Test
    void createInvalidUsersWithNullFields() {
        user1.setEmail(null);
        assertThrows(ValidationException.class, () -> userService.create(user1));

        user2.setLogin(null);
        assertThrows(ValidationException.class, () -> userService.create(user2));

        user3.setName(null);
        userService.create(user3);
        assertEquals(user3.getLogin(), userService.getById(user3.getId()).getName());

        user4.setBirthday(null);
        assertThrows(ValidationException.class, () -> userService.create(user4));
    }


    //корректная работа PUT
    @Test
    void update() {
        userService.create(user1);
        userService.create(user2);
        userService.create(user3);

        user2.setName("User22222222");
        userService.update(user2);
        assertTrue(userService.getAll().contains(user2));
        assertEquals("User22222222", userService.getById(user2.getId()).getName());
    }

    //некорректная работа PUT (условия для валидации)
    @Test
    void updateInvalidUsers() {
        userService.create(user1);
        userService.create(user2);
        userService.create(user3);
        userService.create(user4);
        userService.create(user5);
        userService.create(user6);

        user1.setEmail("    ");
        assertThrows(ValidationException.class, () -> userService.update(user1));

        user2.setEmail("user111mail.ru");
        assertThrows(ValidationException.class, () -> userService.update(user2));

        user3.setLogin("");
        assertThrows(ValidationException.class, () -> userService.update(user3));

        user4.setLogin("    ");
        assertThrows(ValidationException.class, () -> userService.update(user4));

        user5.setName("");
        userService.update(user5);
        assertEquals(user5.getLogin(), userService.getById(user5.getId()).getName());

        user6.setBirthday(LocalDate.of(2023, Month.AUGUST, 1));
        assertThrows(ValidationException.class, () -> userService.update(user6));
    }

    //некорректная работа PUT (null)
    @Test
    void updateInvalidUsersWithNullFields() {
        userService.create(user1);
        userService.create(user2);
        userService.create(user3);
        userService.create(user4);

        user1.setEmail(null);
        assertThrows(ValidationException.class, () -> userService.update(user1));

        user2.setLogin(null);
        assertThrows(ValidationException.class, () -> userService.update(user2));

        user3.setName(null);
        userService.update(user3);
        assertEquals(user3.getLogin(), userService.getById(user3.getId()).getName());

        user4.setBirthday(null);
        assertThrows(ValidationException.class, () -> userService.update(user4));
    }

}