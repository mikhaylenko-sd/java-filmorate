package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    public static final String INVALID = "Валидация не пройдена.";
    public static final String INVALID_NULL = "Передано null-значение";
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User user) {
        validateUser(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        validateUser(user);
        return userStorage.update(user);
    }

    public User getById(int id) {
        validateId(id);
        User user = userStorage.getById(id);
        if (user != null) {
            return user;
        } else {
            throw new UserNotFoundException(id);
        }
    }

    public boolean contains(int id) {
        validateId(id);
        return userStorage.contains(id);
    }

    public User addFriend(int id, int friendId) {
        validateIds(id, friendId);
        User user = getById(id);
        User friendUser = getById(friendId);

        areUsersEqualNull(id, friendId);

        user.getFriends().add(friendId);
        friendUser.getFriends().add(id);
        return user;
    }

    public User removeFriend(int id, int friendId) {
        validateIds(id, friendId);
        User user = getById(id);
        User friendUser = getById(friendId);

        areUsersEqualNull(id, friendId);

        user.getFriends().remove(friendId);
        friendUser.getFriends().remove(id);
        return user;
    }

    public List<User> getUserFriends(int id) {
        validateId(id);
        return getFriendsByUserId(userStorage.getById(id).getFriends());
    }

    public List<User> getCommonFriends(int id, int otherId) {
        validateIds(id, otherId);
        Set<Integer> commonIds = new HashSet<>(userStorage.getById(id).getFriends());
        commonIds.retainAll(userStorage.getById(otherId).getFriends());

        return getFriendsByUserId(commonIds);
    }

    private void validateId(int id) {
        if (id <= 0) {
            throw new IncorrectParameterException("id");
        }
    }

    private void validateIds(int id, int friendId) {
        if (id <= 0 || friendId <= 0) {
            throw new IncorrectParameterException("id");
        }
    }

    private void areUsersEqualNull(int userId, int friendId) {
        if (getById(userId) == null) {
            throw new UserNotFoundException(userId);
        }
        if (getById(friendId) == null) {
            throw new UserNotFoundException(friendId);
        }
    }

    private List<User> getFriendsByUserId(Set<Integer> friendsIds) {
        return friendsIds
                .stream()
                .map(userStorage::getById)
                .collect(Collectors.toList());
    }

    private void validateUser(User user) {
        if (user.getEmail() == null) {
            log.debug(INVALID);
            throw new ValidationException(INVALID_NULL);
        } else if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.debug(INVALID);
            throw new ValidationException("Некорректный ввод электронного адреса.");
        }
        if (user.getLogin() == null) {
            log.debug(INVALID);
            throw new ValidationException(INVALID_NULL);

        } else if (user.getLogin().isBlank()) {
            log.debug(INVALID);
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Валидация пройдена. Вместо имени будет использован логин.");
            user.setName(user.getLogin());
        }
        if (user.getBirthday() == null) {
            log.debug(INVALID);
            throw new ValidationException(INVALID_NULL);

        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug(INVALID);
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }
}