package com.example.CADA.service.impl;

import com.example.CADA.model.Asignacion;
import com.example.CADA.model.EstadoAsignacion;
import com.example.CADA.repository.AsignacionRepository;
import com.example.CADA.service.AsignacionService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class JpaAsignacionService implements AsignacionService {

    private final AsignacionRepository repository;

    public JpaAsignacionService(AsignacionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Asignacion> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Asignacion> findByArbitroId(Long arbitroId) {
        return repository.findByArbitroId(arbitroId);
    }

    @Override
    public List<Asignacion> findByPartidoId(Long partidoId) {
        return repository.findByPartidoId(partidoId);
    }

    @Override
    public Asignacion create(Asignacion asignacion) {
        asignacion.setId(null);
        return repository.save(asignacion);
    }

    @Override
    public Asignacion aceptar(Long asignacionId) {
        Asignacion a = repository.findById(asignacionId).orElseThrow();
        a.setEstado(EstadoAsignacion.ACEPTADA);
        return repository.save(a);
    }

    @Override
    public Asignacion rechazar(Long asignacionId) {
        Asignacion a = repository.findById(asignacionId).orElseThrow();
        a.setEstado(EstadoAsignacion.RECHAZADA);
        return repository.save(a);
    }

    @Override
    public void delete(Long asignacionId) {
        repository.deleteById(asignacionId);
    }
}

