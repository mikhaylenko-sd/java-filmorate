package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.GeneratorId;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final Map<Integer, User> users = new HashMap<>();
    private final GeneratorId generatorId = new GeneratorId();

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        log.debug("Получен запрос POST /user.");
        validateUser(user);
        user.setId(generatorId.generate());
        containsKeyWhenCreate(user);
        return user;
    }

    @Override
    public User update(User user) {
        log.debug("Получен запрос PUT /user.");
        validateUser(user);
        containsKeyWhenUpdate(user);
        return user;
    }

    private void validateUser(User user) {
        if (user.getEmail() == null) {
            log.debug("Валидация не пройдена.");
            throw new ValidationException("Передано null-значение");

        } else if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.debug("Валидация не пройдена.");
            throw new ValidationException("Некорректный ввод электронного адреса.");
        }
        if (user.getLogin() == null) {
            log.debug("Валидация не пройдена.");
            throw new ValidationException("Передано null-значение");

        } else if (user.getLogin().isBlank()) {
            log.debug("Валидация не пройдена.");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Валидация пройдена. Вместо имени будет использован логин.");
            user.setName(user.getLogin());
        }
        if (user.getBirthday() == null) {
            log.debug("Валидация не пройдена.");
            throw new ValidationException("Передано null-значение");

        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Валидация не пройдена.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }

    private void containsKeyWhenCreate(User user) {
        if (!users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new IllegalArgumentException("Вы пытаетесь создать существующего User'а.");
        }
    }

    private void containsKeyWhenUpdate(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new IllegalArgumentException("Вы пытаетесь обновить несуществующего User'а.");
        }
    }
}
