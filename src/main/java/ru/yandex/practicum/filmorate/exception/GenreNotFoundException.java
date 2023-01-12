package ru.yandex.practicum.filmorate.exception;

public class GenreNotFoundException extends RuntimeException {
    int id;

    public GenreNotFoundException(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
