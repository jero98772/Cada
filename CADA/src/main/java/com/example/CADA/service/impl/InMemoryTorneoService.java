package com.example.CADA.service.impl;

import com.example.CADA.model.Torneo;
import com.example.CADA.service.TorneoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class InMemoryTorneoService implements TorneoService {

    private final ConcurrentHashMap<Long, Torneo> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(0);

    @Override
    public List<Torneo> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Torneo> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Torneo create(Torneo torneo) {
        long id = seq.incrementAndGet();
        torneo.setId(id);
        store.put(id, torneo);
        return torneo;
    }

    @Override
    public Torneo update(Long id, Torneo torneo) {
        torneo.setId(id);
        store.put(id, torneo);
        return torneo;
    }

    @Override
    public void delete(Long id) {
        store.remove(id);
    }

    @Override
    public Torneo toggleActivo(Long id) {
        Torneo t = store.get(id);
        if (t != null) {
            t.setActivo(!t.isActivo());
        }
        return t;
    }
}

