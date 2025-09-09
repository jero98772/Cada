package com.example.CADA.controller;

import com.example.CADA.model.*;
import com.example.CADA.repository.AsignacionRepository;
import com.example.CADA.repository.PartidoRepository;
import com.example.CADA.service.ArbitroService;
import com.example.CADA.service.AsignacionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardController {
    private final ArbitroService arbitroService;
    private final AsignacionService asignacionService;
    private final AsignacionRepository asignacionRepository;
    private final PartidoRepository partidoRepository;

    public DashboardController(ArbitroService arbitroService,
                               AsignacionService asignacionService,
                               AsignacionRepository asignacionRepository,
                               PartidoRepository partidoRepository) {
        this.arbitroService = arbitroService;
        this.asignacionService = asignacionService;
        this.asignacionRepository = asignacionRepository;
        this.partidoRepository = partidoRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        String username = auth.getName();
        Arbitro arbitro = arbitroService.findByUsername(username).orElse(null);
        List<Asignacion> asignaciones = arbitro == null ? List.of() : asignacionService.findByArbitroId(arbitro.getId());

        long pend = 0, acep = 0, rech = 0;
        if (arbitro != null) {
            pend = asignacionRepository.countByArbitroIdAndEstado(arbitro.getId(), EstadoAsignacion.PENDIENTE);
            acep = asignacionRepository.countByArbitroIdAndEstado(arbitro.getId(), EstadoAsignacion.ACEPTADA);
            rech = asignacionRepository.countByArbitroIdAndEstado(arbitro.getId(), EstadoAsignacion.RECHAZADA);
        }

        record AsigDet(Long id, Long partidoId, LocalDateTime fechaHora, String fecha, String sede,
                       String local, String visitante, RolArbitro rol, EstadoAsignacion estado) {}

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        List<AsigDet> detalle = asignaciones.stream()
                .map(a -> partidoRepository.findById(a.getPartidoId())
                        .map(p -> new AsigDet(
                                a.getId(), a.getPartidoId(), p.getFechaHora(),
                                p.getFechaHora() != null ? p.getFechaHora().format(fmt) : "-",
                                p.getSede(), p.getEquipoLocal(), p.getEquipoVisitante(),
                                a.getRol(), a.getEstado()))
                        .orElse(new AsigDet(a.getId(), a.getPartidoId(), null, "-", "-", "-", "-", a.getRol(), a.getEstado())))
                .sorted(Comparator.comparing((AsigDet d) -> d.fechaHora == null ? LocalDateTime.MAX : d.fechaHora))
                .collect(Collectors.toList());

        model.addAttribute("arbitro", arbitro);
        model.addAttribute("kpiPendientes", pend);
        model.addAttribute("kpiAceptadas", acep);
        model.addAttribute("kpiRechazadas", rech);
        model.addAttribute("asignaciones", detalle);
        return "dashboard/index";
    }

    @PostMapping("/dashboard/asignacion/{id}/aceptar")
    public String aceptar(@PathVariable Long id) {
        asignacionService.aceptar(id);
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/asignacion/{id}/rechazar")
    public String rechazar(@PathVariable Long id) {
        asignacionService.rechazar(id);
        return "redirect:/dashboard";
    }
}
