package com.example.CADA.seed;

import com.example.CADA.model.*;
import com.example.CADA.service.ArbitroService;
import com.example.CADA.service.AsignacionService;
import com.example.CADA.service.PartidoService;
import com.example.CADA.service.TorneoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
public class MockDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(MockDataLoader.class);

    private final ArbitroService arbitroService;
    private final TorneoService torneoService;
    private final PartidoService partidoService;
    private final AsignacionService asignacionService;

    public MockDataLoader(ArbitroService arbitroService, TorneoService torneoService, PartidoService partidoService, AsignacionService asignacionService) {
        this.arbitroService = arbitroService;
        this.torneoService = torneoService;
        this.partidoService = partidoService;
        this.asignacionService = asignacionService;
    }

    @Override
    public void run(String... args) {
        if (!arbitroService.findAll().isEmpty()) return; // seed once

        // Árbitros
        Arbitro arbitroLogin = arbitroService.create(Arbitro.builder()
                .username("arbitro")
                .nombre("Álvaro")
                .apellido("Pérez")
                .email("arbitro@example.com")
                .especialidad(Especialidad.CAMPO)
                .escalafon(Escalafon.PRIMERA)
                .activo(true)
                .notas("Usuario demo con rol ARBITRO")
                .build());

        arbitroService.create(Arbitro.builder().username("marta").nombre("Marta").apellido("González").email("marta@example.com").especialidad(Especialidad.MESA).escalafon(Escalafon.SEGUNDA).activo(true).build());
        arbitroService.create(Arbitro.builder().username("juan").nombre("Juan").apellido("López").email("juan@example.com").especialidad(Especialidad.CAMPO).escalafon(Escalafon.TERCERA).activo(true).build());

        // Torneos
        Torneo apertura = torneoService.create(Torneo.builder()
                .nombre("Torneo Apertura")
                .temporada("2025")
                .fechaInicio(LocalDate.now().minusMonths(1))
                .fechaFin(LocalDate.now().plusMonths(2))
                .activo(true)
                .build());
        Torneo clausura = torneoService.create(Torneo.builder()
                .nombre("Torneo Clausura")
                .temporada("2025")
                .fechaInicio(LocalDate.now().plusMonths(3))
                .fechaFin(LocalDate.now().plusMonths(6))
                .activo(false)
                .build());

        // Partidos
        Random rnd = new Random(42);
        for (int i = 0; i < 10; i++) {
            Partido p = partidoService.create(Partido.builder()
                    .torneoId(apertura.getId())
                    .fechaHora(LocalDateTime.now().plusDays(rnd.nextInt(20) - 10))
                    .sede("Cancha " + (i + 1))
                    .equipoLocal("Equipo " + (char)('A' + i))
                    .equipoVisitante("Equipo " + (char)('L' + i))
                    .build());
            asignacionService.create(Asignacion.builder()
                    .partidoId(p.getId())
                    .arbitroId(arbitroLogin.getId())
                    .rol(RolArbitro.PRINCIPAL)
                    .estado(i % 3 == 0 ? EstadoAsignacion.ACEPTADA : (i % 3 == 1 ? EstadoAsignacion.RECHAZADA : EstadoAsignacion.PENDIENTE))
                    .build());
        }

        log.info("Datos de ejemplo creados: {} árbitros, {} torneos, {} partidos, {} asignaciones",
                arbitroService.findAll().size(),
                torneoService.findAll().size(),
                partidoService.findAll().size(),
                asignacionService.findAll().size());
    }
}

