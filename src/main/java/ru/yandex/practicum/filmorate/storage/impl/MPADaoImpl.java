package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.MPAMapper;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPADao;

import java.util.List;

@Component
public class MPADaoImpl implements MPADao {
    private JdbcTemplate jdbcTemplate;

    public MPADaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MPA> getAll() {
        String sql = "SELECT * FROM MPA";
        return jdbcTemplate.query(sql, new MPAMapper());
    }

    @Override
    public MPA getById(int MPAId) {
        try {
            String sql = "SELECT * FROM MPA WHERE id=?";
            return jdbcTemplate.queryForObject(sql, new MPAMapper(), MPAId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
