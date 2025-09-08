package com.example.CADA.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Partido {
    private Long id;
    private Long torneoId;
    private LocalDateTime fechaHora;
    private String sede;
    private String equipoLocal;
    private String equipoVisitante;
}

