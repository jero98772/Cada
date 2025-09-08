package com.example.CADA.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Asignacion {
    private Long id;
    private Long partidoId;
    private Long arbitroId;
    private RolArbitro rol;
    private EstadoAsignacion estado;
}

