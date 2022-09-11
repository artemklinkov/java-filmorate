package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.service.MPAService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
public class MPAController {

    private MPAService mpaService;

    @Autowired
    public MPAController(MPAService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping("/{id}")
    public MPARating getMPARatingById(@PathVariable int id) {
        return mpaService.getMPARatingById(id);
    }

    @GetMapping
    public Collection<MPARating> getAllFilms() {
        return mpaService.getAllMPARatings();
    }

}
