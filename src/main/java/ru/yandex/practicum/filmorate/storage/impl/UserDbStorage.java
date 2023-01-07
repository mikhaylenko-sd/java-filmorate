package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.mapper.IdMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component("UserDbStorage")
@Primary
public class UserDbStorage implements UserStorage {
    private JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT *  FROM users;";
        return jdbcTemplate.query(sql, new UserMapper());
    }

    @Override
    public User getById(int id) {
        if (contains(id)) {
            String sql = "SELECT *  FROM users u" +
                    " WHERE u.user_id=?;";
            return jdbcTemplate.query(sql, new Object[]{id}, new UserMapper()).stream()
                    .findAny().orElse(null);
        } else {
            throw new UserNotFoundException(id);
        }
    }

    @Override
    public User create(User user) {
        if (!contains(user.getId())) {
            String sql = "INSERT INTO users(name, birthdate, email, login) VALUES(?,?,?,?);";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"user_id"});
                stmt.setString(1, user.getName());
                stmt.setDate(2, Date.valueOf(user.getBirthday()));
                stmt.setString(3, user.getEmail());
                stmt.setString(4, user.getLogin());
                return stmt;
            }, keyHolder);
            user.setId(keyHolder.getKey().intValue());
            return user;
        } else {
            throw new IllegalArgumentException("Вы пытаетесь создать существующего User'а");
        }
    }

    @Override
    public User update(User user) {
        if (contains(user.getId())) {
            String sql = "UPDATE users SET name=?, birthdate=?, email=?, login=? WHERE user_id=?;";
            jdbcTemplate.update(sql, user.getName(), user.getBirthday(), user.getEmail(), user.getLogin(), user.getId());
            return user;
        } else {
            throw new UserNotFoundException(user.getId());
        }
    }

    @Override
    public boolean contains(int id) {
        String sql = "SELECT COUNT(*) FROM users WHERE user_id = ?;";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
        return count != null && count != 0;
    }

    @Override
    public Set<Integer> getUserFriendsIds(int id) {
        if (contains(id)) {
            String sql = "SELECT f.user_id2 AS id FROM friendship f " +
                    "LEFT JOIN users u ON f.user_id2=u.user_id WHERE f.user_id1=?;";
            return new HashSet<>(jdbcTemplate.query(sql, new IdMapper(), id));
        } else {
            throw new UserNotFoundException(id);
        }
    }

    @Override
    public User addFriend(int id, int friendId) {
        areUsersExist(id, friendId);
        String sql = "MERGE INTO friendship(user_id1, user_id2) VALUES(?, ?)";
        jdbcTemplate.update(sql, id, friendId);

        return getById(friendId);
    }

    @Override
    public User removeFriend(int id, int friendId) {
        areUsersExist(id, friendId);
        String sql = "DELETE FROM friendship WHERE user_id1=? AND user_id2=?;";
        jdbcTemplate.update(sql, id, friendId);

        return getById(friendId);
    }

    private void areUsersExist(int id, int friendId) {
        if (!contains(id)) {
            throw new UserNotFoundException(id);
        }
        if (!contains(friendId)) {
            throw new UserNotFoundException(friendId);
        }
    }
}
