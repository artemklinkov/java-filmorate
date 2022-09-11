package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.mpa.MPAStorage;

import java.util.Collection;

@Slf4j
@Service
public class MPAService {
    private MPAStorage mpaStorage;

    @Autowired
    public MPAService(MPAStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Collection<MPARating> getAllMPARatings() {
        log.debug("Получение всех рейтингов фильмов");
        return mpaStorage.getAllMPARatings();
    }

    public MPARating getMPARatingById(int id) {
        return mpaStorage.getMPARatingById(id);
    }
}
