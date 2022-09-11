package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.Collection;

public interface MPAStorage {
    Collection<MPARating> getAllMPARatings();

    MPARating getMPARatingById(int id);
}
