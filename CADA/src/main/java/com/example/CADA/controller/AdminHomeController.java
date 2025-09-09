package com.example.CADA.controller;

import com.example.CADA.model.EstadoAsignacion;
import com.example.CADA.repository.ArbitroRepository;
import com.example.CADA.repository.AsignacionRepository;
import com.example.CADA.repository.PartidoRepository;
import com.example.CADA.repository.TorneoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class AdminHomeController {

    private final ArbitroRepository arbitroRepository;
    private final TorneoRepository torneoRepository;
    private final PartidoRepository partidoRepository;
    private final AsignacionRepository asignacionRepository;

    public AdminHomeController(ArbitroRepository arbitroRepository,
                               TorneoRepository torneoRepository,
                               PartidoRepository partidoRepository,
                               AsignacionRepository asignacionRepository) {
        this.arbitroRepository = arbitroRepository;
        this.torneoRepository = torneoRepository;
        this.partidoRepository = partidoRepository;
        this.asignacionRepository = asignacionRepository;
    }

    @GetMapping("/admin")
    public String index(Model model) {
        long arbitrosActivos = arbitroRepository.countByActivo(true);
        long arbitrosInactivos = arbitroRepository.countByActivo(false);
        long torneosActivos = torneoRepository.countByActivo(true);
        long asignPend = asignacionRepository.countByEstado(EstadoAsignacion.PENDIENTE);
        long asignAcep = asignacionRepository.countByEstado(EstadoAsignacion.ACEPTADA);
        long asignRech = asignacionRepository.countByEstado(EstadoAsignacion.RECHAZADA);

        model.addAttribute("arbitrosActivos", arbitrosActivos);
        model.addAttribute("arbitrosInactivos", arbitrosInactivos);
        model.addAttribute("torneosActivos", torneosActivos);
        model.addAttribute("asignPend", asignPend);
        model.addAttribute("asignAcep", asignAcep);
        model.addAttribute("asignRech", asignRech);
        model.addAttribute("proximosPartidos",
                partidoRepository.findTop5ByFechaHoraAfterOrderByFechaHoraAsc(LocalDateTime.now()));
        return "admin/index";
    }
}

