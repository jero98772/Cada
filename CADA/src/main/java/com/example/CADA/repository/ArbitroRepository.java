package com.example.CADA.repository;

import com.example.CADA.model.Arbitro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArbitroRepository extends JpaRepository<Arbitro, Long> {
    Optional<Arbitro> findByUsername(String username);
    long countByActivo(boolean activo);
}

