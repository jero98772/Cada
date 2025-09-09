package com.example.CADA.service.impl;

import com.example.CADA.model.Partido;
import com.example.CADA.repository.PartidoRepository;
import com.example.CADA.service.PartidoService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class JpaPartidoService implements PartidoService {

    private final PartidoRepository repository;

    public JpaPartidoService(PartidoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Partido> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Partido> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Partido create(Partido partido) {
        partido.setId(null);
        return repository.save(partido);
    }
}

