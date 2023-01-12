package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MPAService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MPAController {
    private final MPAService MPAService;

    @Autowired
    public MPAController(MPAService MPAService) {
        this.MPAService = MPAService;
    }

    @GetMapping
    public List<MPA> getAll() {
        return MPAService.getAll();
    }

    @GetMapping(value = "/{id}")
    public MPA getById(@PathVariable int id) {
        return MPAService.getById(id);
    }
}
