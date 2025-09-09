package com.example.CADA.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "asignaciones")
public class Asignacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long partidoId;
    private Long arbitroId;

    @Enumerated(EnumType.STRING)
    private RolArbitro rol;

    @Enumerated(EnumType.STRING)
    private EstadoAsignacion estado;
}

