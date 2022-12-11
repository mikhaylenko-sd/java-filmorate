package ru.yandex.practicum.filmorate.exception;

public class FilmNotFoundException extends RuntimeException {
    int filmId;

    public FilmNotFoundException(int filmId) {
        this.filmId = filmId;
    }

    public int getFilmId() {
        return filmId;
    }
}
