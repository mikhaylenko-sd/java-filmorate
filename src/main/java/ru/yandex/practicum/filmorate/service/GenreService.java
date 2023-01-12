package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDao;

import java.util.List;

@Service
public class GenreService {
    private final GenreDao genreDao;

    @Autowired
    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public List<Genre> getAll() {
        return genreDao.getAll();
    }

    public Genre getById(int genreId) {
        if (genreDao.getById(genreId) != null) {
            return genreDao.getById(genreId);
        } else {
            throw new GenreNotFoundException(genreId);
        }
    }
}
