package ru.yandex.practicum.filmorate.storage.impl.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.GeneratorId;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Slf4j
@Component("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private final GeneratorId generatorId = new GeneratorId();

    @Override
    public List<User> getAll() {
        log.debug("Получен список пользователей");
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        user.setId(generatorId.generate());
        createIfContainsKey(user);
        log.debug("Создан пользователь");
        return user;
    }

    @Override
    public User update(User user) {
        updateIfContainsKey(user);
        log.debug("Обновлен пользователь");
        return user;
    }

    @Override
    public User getById(int id) {
        return users.get(id);
    }

    @Override
    public boolean contains(int id) {
        return users.containsKey(id);
    }

    private void createIfContainsKey(User user) { //
        if (!users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new IllegalArgumentException("Вы пытаетесь создать существующего User'а.");
        }
    }

    private void updateIfContainsKey(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new IllegalArgumentException("Вы пытаетесь обновить несуществующего User'а.");
        }
    }

    @Override
    public Set<Integer> getUserFriendsIds(int id) {
        return getById(id).getFriends();
    }

    @Override
    public User addFriend(int id, int friendId) {
        User user = getById(id);
        User friendUser = getById(friendId);

        areUsersEqualNull(id, friendId);

        user.getFriends().add(friendId);
        friendUser.getFriends().add(id);
        return user;
    }

    @Override
    public User removeFriend(int id, int friendId) {
        User user = getById(id);
        User friendUser = getById(friendId);

        areUsersEqualNull(id, friendId);

        user.getFriends().remove(friendId);
        friendUser.getFriends().remove(id);
        return user;
    }

    private void areUsersEqualNull(int userId, int friendId) {
        if (getById(userId) == null) {
            throw new UserNotFoundException(userId);
        }
        if (getById(friendId) == null) {
            throw new UserNotFoundException(friendId);
        }
    }

}
