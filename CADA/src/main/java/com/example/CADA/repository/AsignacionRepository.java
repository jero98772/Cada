package com.example.CADA.repository;

import com.example.CADA.model.Asignacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {
    List<Asignacion> findByArbitroId(Long arbitroId);
}

