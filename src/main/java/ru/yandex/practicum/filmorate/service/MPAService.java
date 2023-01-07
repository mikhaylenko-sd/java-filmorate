package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MPANotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPADao;

import java.util.List;

@Service
public class MPAService {
    private final MPADao mpaDao;

    @Autowired
    public MPAService(MPADao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public List<MPA> getAll() {
        return mpaDao.getAll();
    }

    public MPA getById(int ratingId) {
        MPA mpa = mpaDao.getById(ratingId);
        if (mpa != null) {
            return mpa;
        } else {
            throw new MPANotFoundException(ratingId);
        }
    }
}
