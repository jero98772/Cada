package com.example.CADA.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Arbitro {
    private Long id;
    private String username; // para asociar con el usuario autenticado
    private String nombre;
    private String apellido;
    private String email;
    private Especialidad especialidad;
    private Escalafon escalafon;
    private boolean activo;
    private String notas;
}

