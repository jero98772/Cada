package com.example.CADA.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Torneo {
    private Long id;
    private String nombre;
    private String temporada; // por ejemplo, 2024/2025
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean activo;
}

