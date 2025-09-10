package com.example.CADA.service.impl;

import com.example.CADA.model.Asignacion;
import com.example.CADA.model.EstadoAsignacion;
import com.example.CADA.service.AsignacionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// @Service // deshabilitado: implementación en memoria
public class InMemoryAsignacionService implements AsignacionService {

    private final ConcurrentHashMap<Long, Asignacion> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(0);

    @Override
    public List<Asignacion> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Asignacion> findByArbitroId(Long arbitroId) {
        return store.values().stream()
                .filter(a -> a.getArbitroId() != null && a.getArbitroId().equals(arbitroId))
                .toList();
    }

    @Override
    public List<Asignacion> findByPartidoId(Long partidoId) {
        return store.values().stream()
                .filter(a -> a.getPartidoId() != null && a.getPartidoId().equals(partidoId))
                .toList();
    }

    @Override
    public Asignacion create(Asignacion asignacion) {
        long id = seq.incrementAndGet();
        asignacion.setId(id);
        store.put(id, asignacion);
        return asignacion;
    }

    @Override
    public Asignacion aceptar(Long asignacionId) {
        return updateEstado(asignacionId, EstadoAsignacion.ACEPTADA);
    }

    @Override
    public Asignacion rechazar(Long asignacionId) {
        return updateEstado(asignacionId, EstadoAsignacion.RECHAZADA);
    }

    @Override
    public void delete(Long asignacionId) {
        store.remove(asignacionId);
    }

    private Asignacion updateEstado(Long id, EstadoAsignacion estado) {
        Asignacion a = store.get(id);
        if (a != null) {
            a.setEstado(estado);
        }
        return a;
    }
}

