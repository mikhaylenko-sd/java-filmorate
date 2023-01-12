package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
@Component
public interface GenreDao {
    List<Genre> getAll();

    Genre getById(int genreId);

}
