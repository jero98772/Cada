package com.example.CADA.service.impl;

import com.example.CADA.model.Arbitro;
import com.example.CADA.repository.ArbitroRepository;
import com.example.CADA.service.ArbitroService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class JpaArbitroService implements ArbitroService {

    private final ArbitroRepository repository;

    public JpaArbitroService(ArbitroRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Arbitro> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Arbitro> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Arbitro> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public Arbitro create(Arbitro arbitro) {
        arbitro.setId(null);
        return repository.save(arbitro);
    }

    @Override
    public Arbitro update(Long id, Arbitro arbitro) {
        arbitro.setId(id);
        return repository.save(arbitro);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Arbitro toggleActivo(Long id) {
        Arbitro a = repository.findById(id).orElseThrow();
        a.setActivo(!a.isActivo());
        return repository.save(a);
    }
}

