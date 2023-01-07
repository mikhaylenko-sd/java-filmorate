package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDao;

import java.util.List;

@Component
public class GenreDaoImpl implements GenreDao {
    private JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAll() {
        String sql = "SELECT * FROM genres;";
        return jdbcTemplate.query(sql, new GenreMapper());
    }

    @Override
    public Genre getById(int genreId) throws EmptyResultDataAccessException {
        try {
            String sql = "SELECT * FROM genres WHERE id=?";
            return jdbcTemplate.queryForObject(sql, new GenreMapper(), genreId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
