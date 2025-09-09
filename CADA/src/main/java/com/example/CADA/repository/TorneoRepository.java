package com.example.CADA.repository;

import com.example.CADA.model.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TorneoRepository extends JpaRepository<Torneo, Long> {
    long countByActivo(boolean activo);
}

