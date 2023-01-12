package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

@Component
public interface UserStorage {
    List<User> getAll();

    User create(User user);

    User update(User user);

    User getById(int id);

    boolean contains(int id);

    Set<Integer> getUserFriendsIds(int id);

    User addFriend(int id, int friendId);

    User removeFriend(int id, int friendId);
}
