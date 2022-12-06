package ru.yandex.practicum.filmorate;

public class GeneratorId {
    private int id;

    public int generate() {
        id++;
        return id;
    }
}
