package com.example.CADA.repository;

import com.example.CADA.model.Asignacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.example.CADA.model.EstadoAsignacion;

public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {
    List<Asignacion> findByArbitroId(Long arbitroId);
    List<Asignacion> findByPartidoId(Long partidoId);
    long countByEstado(EstadoAsignacion estado);
    long countByArbitroIdAndEstado(Long arbitroId, EstadoAsignacion estado);
}

