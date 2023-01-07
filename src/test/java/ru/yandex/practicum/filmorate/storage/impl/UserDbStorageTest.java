package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

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
class UserDbStorageTest {
    private final UserDbStorage userStorage;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = userStorage.create(new User(0, "user111@mail.ru", "login 1", "User 1", LocalDate.of(2001, Month.AUGUST, 1)));
        user2 = userStorage.create(new User(0, "user222@mail.ru", "login 2", "User 2", LocalDate.of(2001, Month.AUGUST, 1)));
    }

    @Test
    void getAll() {
        List<User> users = userStorage.getAll();
        assertEquals(2, users.size());
    }

    @Test
    void getUserById() {
        User user = userStorage.getById(user1.getId());
        assertEquals(user1, user);

        user = userStorage.getById(user2.getId());
        assertEquals(user2, user);
    }

    @Test
    void create() {
        assertEquals(2, userStorage.getAll().size());
        User user3 = new User(0, "user333@mail.ru", "login 3", "User 3", LocalDate.of(2003, Month.APRIL, 3));
        userStorage.create(user3);

        assertEquals(3, userStorage.getAll().size());
    }

    @Test
    void update() {
        user1.setName("11User11");
        user1.setBirthday(LocalDate.of(2000, Month.APRIL, 3));
        userStorage.update(user1);

        User user = userStorage.getById(user1.getId());
        assertEquals("11User11", user.getName());
        assertEquals(LocalDate.of(2000, Month.APRIL, 3), user.getBirthday());
    }

    @Test
    void contains() {
        assertTrue(userStorage.contains(1));
        assertTrue(userStorage.contains(2));

        assertFalse(userStorage.contains(3));
    }

    @Test
    void getUserFriendsIds() {
        User user3 = new User(0, "user333@mail.ru", "login 3", "User 3", LocalDate.of(2003, Month.APRIL, 3));
        userStorage.create(user3);

        int id = user1.getId();
        assertEquals(new HashSet<>(), userStorage.getUserFriendsIds(id));

        int friendId1 = user2.getId();
        int friendId2 = user3.getId();
        userStorage.addFriend(id, friendId1);
        userStorage.addFriend(id, friendId2);

        assertEquals(Set.of(friendId1, friendId2), userStorage.getUserFriendsIds(id));
    }

    @Test
    void addFriend() {
        int id = user1.getId();
        int friendId = user2.getId();
        assertEquals(new HashSet<>(), userStorage.getUserFriendsIds(id));

        userStorage.addFriend(id, friendId);

        assertEquals(Set.of(friendId), userStorage.getUserFriendsIds(id));
    }

    @Test
    void removeFriend() {
        int id = user1.getId();
        int friendId = user2.getId();
        userStorage.addFriend(id, friendId);
        assertEquals(Set.of(friendId), userStorage.getUserFriendsIds(id));

        userStorage.removeFriend(id, friendId);

        assertEquals(new HashSet<>(), userStorage.getUserFriendsIds(id));
    }
}
