package ru.yandex.practicum.filmorate.exception;

public class UserNotFoundException extends RuntimeException {
    int userId;

    public UserNotFoundException(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }
}
