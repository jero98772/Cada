package com.example.CADA.service;

import com.example.CADA.model.Partido;

import java.util.List;
import java.util.Optional;

public interface PartidoService {
    List<Partido> findAll();
    Optional<Partido> findById(Long id);
    Partido create(Partido partido);
}

