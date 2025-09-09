package com.example.CADA.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "partidos")
public class Partido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Simplificado: referencia por ID en lugar de relación
    private Long torneoId;

    private LocalDateTime fechaHora;
    private String sede;
    private String equipoLocal;
    private String equipoVisitante;
}

