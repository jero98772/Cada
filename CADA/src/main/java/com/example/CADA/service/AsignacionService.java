package com.example.CADA.service;

import com.example.CADA.model.Asignacion;

import java.util.List;

public interface AsignacionService {
    List<Asignacion> findAll();
    List<Asignacion> findByArbitroId(Long arbitroId);
    Asignacion create(Asignacion asignacion);
    Asignacion aceptar(Long asignacionId);
    Asignacion rechazar(Long asignacionId);
}

