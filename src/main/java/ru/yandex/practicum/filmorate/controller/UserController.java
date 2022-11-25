package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.GeneratorId;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final HashMap<Integer, User> users = new HashMap<>();

    private final GeneratorId generatorId = new GeneratorId();

    @GetMapping("/users")
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping(value = "/users")
    public User create(@RequestBody User user) {
        log.debug("Получен запрос POST /user.");
        userValidate(user);
        user.setId(generatorId.generate());
        if (!users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new IllegalArgumentException("Вы пытаетесь создать существующего User'а.");
        }
        return user;
    }

    @PutMapping(value = "/users")
    public User update(@RequestBody User user) {
        log.debug("Получен запрос PUT /user.");
        userValidate(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new IllegalArgumentException("Вы пытаетесь обновить несуществующего User'а.");
        }
        return user;
    }

    private void userValidate(User user) {
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
}
