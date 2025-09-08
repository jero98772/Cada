package com.example.CADA.service;

import com.example.CADA.model.Torneo;

import java.util.List;
import java.util.Optional;

public interface TorneoService {
    List<Torneo> findAll();
    Optional<Torneo> findById(Long id);
    Torneo create(Torneo torneo);
    Torneo update(Long id, Torneo torneo);
    void delete(Long id);
    Torneo toggleActivo(Long id);
}

