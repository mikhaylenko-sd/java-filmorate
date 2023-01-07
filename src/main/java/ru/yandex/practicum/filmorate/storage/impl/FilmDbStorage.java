package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.mapper.IdMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Slf4j
@Component("FilmDbStorage")
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT f.*, m.name AS mpa_name FROM films f" +
                " LEFT JOIN MPA m ON m.id = F.mpa_id;";
        List<Film> films = jdbcTemplate.query(sql, new FilmMapper());

        for (Film film : films) {
            fillLikesAndGenres(film);
        }
        return films;
    }

    private void fillLikesAndGenres(Film film) {
        int filmId = film.getId();
        String sql = "SELECT g.* FROM film_genre AS fg " +
                "JOIN genres g ON g.id=fg.genre_id " +
                "WHERE fg.film_id=? " +
                "ORDER BY fg.genre_id;";
        List<Genre> genres = jdbcTemplate.query(sql, new GenreMapper(), filmId);
        film.getGenres().clear();
        film.getGenres().addAll(genres);

        sql = "SELECT user_id AS id FROM likes " +
                "WHERE film_id = ?;";
        List<Integer> likes = jdbcTemplate.query(sql, new IdMapper(), filmId);
        film.getLikes().clear();
        film.getLikes().addAll(likes);
    }

    @Override
    public void addLike(int id, int userId) {
        if (contains(id)) {
            String sql = "MERGE INTO likes VALUES(?, ?);";
            jdbcTemplate.update(sql, id, userId);
        } else {
            throw new FilmNotFoundException(id);
        }
    }

    @Override
    public void removeLike(int id, int userId) {
        if (contains(id)) {
            String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?;";
            jdbcTemplate.update(sql, id, userId);
        } else {
            throw new FilmNotFoundException(id);
        }
    }

    @Override
    public Film getById(int id) {
        if (contains(id)) {
            String sql = "SELECT f.*, m.name AS mpa_name FROM films f" +
                    " LEFT JOIN MPA m ON m.id = f.mpa_id" +
                    " WHERE f.film_id=?;";
            Film film = jdbcTemplate.query(sql, new Object[]{id}, new FilmMapper()).stream()
                    .findAny().orElse(null);
            if (film != null) {
                fillLikesAndGenres(film);
                return film;
            } else {
                throw new FilmNotFoundException(id);
            }
        } else {
            throw new FilmNotFoundException(id);
        }
    }

    @Override
    public Film create(Film film) {
        if (!contains(film.getId())) {
            String sql = "INSERT INTO films(name, description,release_date, duration,MPA_id) VALUES(?,?,?,?,?);";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"film_id"});
                stmt.setString(1, film.getName());
                stmt.setString(2, film.getDescription());
                stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
                stmt.setLong(4, film.getDuration());
                stmt.setInt(5, film.getMpa().getId());
                return stmt;
            }, keyHolder);
            film.setId(keyHolder.getKey().intValue());
            addGenres(film);
            fillLikesAndGenres(film);
            return film;
        } else {
            throw new IllegalArgumentException("Вы пытаетесь создать существующий Film");
        }
    }

    @Override
    public Film update(Film film) {
        if (contains(film.getId())) {
            String sql = "UPDATE films SET name=?, description=?,release_date=?, duration=?,MPA_id=? WHERE film_ID=?;";
            jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                    film.getDuration(), film.getMpa().getId(), film.getId());
            addGenres(film);
            fillLikesAndGenres(film);
            return film;
        } else {
            throw new FilmNotFoundException(film.getId());
        }
    }

    private void addGenres(Film film) {
        String sql = "DELETE FROM film_genre WHERE film_id = ?;";
        jdbcTemplate.update(sql, film.getId());

        sql = "MERGE INTO film_genre VALUES (?, ?);";
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(sql, film.getId(), genre.getId());
        }
    }

    @Override
    public boolean contains(int id) {
        String sql = "SELECT COUNT(*) FROM films WHERE film_id = ?;";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
        return count != null && count != 0;
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM films;";
        jdbcTemplate.update(sql);
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM films WHERE film_id=?;";
        jdbcTemplate.update(sql, id);
    }

}
