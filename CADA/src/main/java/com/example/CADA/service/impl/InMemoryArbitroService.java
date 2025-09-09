package com.example.CADA.service.impl;

import com.example.CADA.model.Arbitro;
import com.example.CADA.service.ArbitroService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// @Service // deshabilitado: implementación en memoria
public class InMemoryArbitroService implements ArbitroService {

    private final ConcurrentHashMap<Long, Arbitro> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(0);

    @Override
    public List<Arbitro> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Arbitro> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Arbitro> findByUsername(String username) {
        return store.values().stream()
                .filter(a -> username != null && username.equalsIgnoreCase(a.getUsername()))
                .findFirst();
    }

    @Override
    public Arbitro create(Arbitro arbitro) {
        long id = seq.incrementAndGet();
        arbitro.setId(id);
        store.put(id, arbitro);
        return arbitro;
    }

    @Override
    public Arbitro update(Long id, Arbitro arbitro) {
        arbitro.setId(id);
        store.put(id, arbitro);
        return arbitro;
    }

    @Override
    public void delete(Long id) {
        store.remove(id);
    }

    @Override
    public Arbitro toggleActivo(Long id) {
        Arbitro a = store.get(id);
        if (a != null) {
            a.setActivo(!a.isActivo());
        }
        return a;
    }
}

