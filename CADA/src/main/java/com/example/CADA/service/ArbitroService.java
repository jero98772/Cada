package com.example.CADA.service;

import com.example.CADA.model.Arbitro;

import java.util.List;
import java.util.Optional;

public interface ArbitroService {
    List<Arbitro> findAll();
    Optional<Arbitro> findById(Long id);
    Optional<Arbitro> findByUsername(String username);
    Arbitro create(Arbitro arbitro);
    Arbitro update(Long id, Arbitro arbitro);
    void delete(Long id);
    Arbitro toggleActivo(Long id);
}

