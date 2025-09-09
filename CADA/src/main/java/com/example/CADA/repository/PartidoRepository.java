package com.example.CADA.repository;

import com.example.CADA.model.Partido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PartidoRepository extends JpaRepository<Partido, Long> {
    List<Partido> findTop5ByFechaHoraAfterOrderByFechaHoraAsc(LocalDateTime now);
}

