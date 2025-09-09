package com.example.CADA.service.impl;

import com.example.CADA.model.Torneo;
import com.example.CADA.repository.TorneoRepository;
import com.example.CADA.service.TorneoService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class JpaTorneoService implements TorneoService {

    private final TorneoRepository repository;

    public JpaTorneoService(TorneoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Torneo> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Torneo> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Torneo create(Torneo torneo) {
        torneo.setId(null);
        return repository.save(torneo);
    }

    @Override
    public Torneo update(Long id, Torneo torneo) {
        torneo.setId(id);
        return repository.save(torneo);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Torneo toggleActivo(Long id) {
        Torneo t = repository.findById(id).orElseThrow();
        t.setActivo(!t.isActivo());
        return repository.save(t);
    }
}

