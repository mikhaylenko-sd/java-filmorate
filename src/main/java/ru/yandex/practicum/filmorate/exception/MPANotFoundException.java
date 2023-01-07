package ru.yandex.practicum.filmorate.exception;

public class MPANotFoundException extends RuntimeException {
    int id;

    public MPANotFoundException(int id) {
        this.id = id;
    }

    public int getMPAId() {
        return id;
    }
}
