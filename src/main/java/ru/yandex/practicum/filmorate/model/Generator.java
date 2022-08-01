package ru.yandex.practicum.filmorate.model;

public class Generator {
    private int nextID;

    public Generator() {
        this.nextID = 1;
    }

    public int getNextID() {
        return nextID++;
    }
}
