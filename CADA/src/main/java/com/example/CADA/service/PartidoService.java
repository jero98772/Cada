package com.example.CADA.service;

import com.example.CADA.model.Partido;

import java.util.List;
import java.util.Optional;

public interface PartidoService {
    List<Partido> findAll();
    Optional<Partido> findById(Long id);
    List<Partido> findByTorneoId(Long torneoId);
    List<Partido> findUnassigned();
    Partido create(Partido partido);
    Partido update(Long id, Partido partido);
    void delete(Long id);
}

