package com.example.CADA.service.impl;

import com.example.CADA.model.Partido;
import com.example.CADA.service.PartidoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// @Service // deshabilitado: implementación en memoria
public class InMemoryPartidoService implements PartidoService {

    private final ConcurrentHashMap<Long, Partido> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(0);

    @Override
    public List<Partido> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Partido> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Partido create(Partido partido) {
        long id = seq.incrementAndGet();
        partido.setId(id);
        store.put(id, partido);
        return partido;
    }
}

