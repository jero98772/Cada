package com.example.CADA.controller;

import com.example.CADA.model.*;
import com.example.CADA.service.ArbitroService;
import com.example.CADA.service.AsignacionService;
import com.example.CADA.service.PartidoService;
import com.example.CADA.service.TorneoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/partidos")
public class AdminPartidoController {

    private static final Logger log = LoggerFactory.getLogger(AdminPartidoController.class);

    private final PartidoService partidoService;
    private final TorneoService torneoService;
    private final ArbitroService arbitroService;
    private final AsignacionService asignacionService;

    public AdminPartidoController(PartidoService partidoService,
                                  TorneoService torneoService,
                                  ArbitroService arbitroService,
                                  AsignacionService asignacionService) {
        this.partidoService = partidoService;
        this.torneoService = torneoService;
        this.arbitroService = arbitroService;
        this.asignacionService = asignacionService;
    }

    @GetMapping
    public String list(Model model) {
        List<Partido> partidos = partidoService.findAll();
        List<Torneo> torneos = torneoService.findAll();
        Map<Long, String> torneoMap = torneos.stream()
                .collect(Collectors.toMap(Torneo::getId, Torneo::getNombre));
        Map<Long, String> arbitroMap = arbitroService.findActivos().stream()
                .collect(Collectors.toMap(Arbitro::getId, a -> a.getNombre() + " " + a.getApellido()));

        record Row(Long id, java.time.LocalDateTime fechaHora, String sede,
                   String local, String visitante, Long torneoId, String torneoNombre,
                   Long arbitroId, String arbitroNombre) {}
        List<Row> rows = partidos.stream()
                .map(p -> new Row(
                        p.getId(), p.getFechaHora(), p.getSede(), p.getEquipoLocal(), p.getEquipoVisitante(),
                        p.getTorneoId(), p.getTorneoId() != null ? torneoMap.getOrDefault(p.getTorneoId(), "-") : null,
                        p.getArbitroId(), p.getArbitroId() != null ? arbitroMap.getOrDefault(p.getArbitroId(), "-") : null
                ))
                .collect(java.util.stream.Collectors.toList());

        model.addAttribute("rows", rows);
        return "admin/partidos/list";
    }

    @GetMapping("/nuevo")
    public String createForm(Model model) {
        model.addAttribute("partido", new Partido());
        model.addAttribute("torneos", torneoService.findAll());
        List<Arbitro> lista = arbitroService.findAll();
        log.debug("Arbitros para formulario (findAll): size={}", lista.size());
        model.addAttribute("arbitros", lista);
        return "admin/partidos/form";
    }

    @PostMapping
    public String create(@ModelAttribute @Valid Partido partido, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("torneos", torneoService.findAll());
            List<Arbitro> lista = arbitroService.findAll();
            model.addAttribute("arbitros", lista);
            return "admin/partidos/form";
        }
        partidoService.create(partido);
        return "redirect:/admin/partidos";
    }

    @GetMapping("/{id}/editar")
    public String editForm(@PathVariable Long id, Model model) {
        Partido p = partidoService.findById(id).orElseThrow();
        model.addAttribute("partido", p);
        model.addAttribute("torneos", torneoService.findAll());
        List<Arbitro> lista = arbitroService.findAll();
        model.addAttribute("arbitros", lista);
        return "admin/partidos/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute @Valid Partido partido, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("torneos", torneoService.findAll());
            List<Arbitro> lista = arbitroService.findAll();
            model.addAttribute("arbitros", lista);
            return "admin/partidos/form";
        }
        partidoService.update(id, partido);
        return "redirect:/admin/partidos";
    }

    @PostMapping("/{id}/eliminar")
    public String delete(@PathVariable Long id) {
        partidoService.delete(id);
        return "redirect:/admin/partidos";
    }

    // Gestión de asignaciones de árbitros por partido
    @GetMapping("/{id}/asignaciones")
    public String asignaciones(@PathVariable Long id, Model model) {
        Partido p = partidoService.findById(id).orElseThrow();
        List<Asignacion> asignaciones = asignacionService.findByPartidoId(id);
        List<Arbitro> arbitros = arbitroService.findActivos();
        Map<Long, String> arbitroMap = arbitros.stream()
                .collect(Collectors.toMap(Arbitro::getId, a -> a.getNombre() + " " + a.getApellido()));
        model.addAttribute("partido", p);
        model.addAttribute("asignaciones", asignaciones);
        model.addAttribute("arbitros", arbitros);
        model.addAttribute("arbitroMap", arbitroMap);
        model.addAttribute("roles", RolArbitro.values());
        return "admin/partidos/asignaciones";
    }

    @PostMapping("/{id}/asignaciones")
    public String crearAsignacion(@PathVariable Long id, @RequestParam Long arbitroId, @RequestParam RolArbitro rol) {
        Asignacion a = Asignacion.builder()
                .partidoId(id)
                .arbitroId(arbitroId)
                .rol(rol)
                .estado(EstadoAsignacion.PENDIENTE)
                .build();
        asignacionService.create(a);
        return "redirect:/admin/partidos/" + id + "/asignaciones";
    }

    @PostMapping("/{id}/asignaciones/{asigId}/eliminar")
    public String eliminarAsignacion(@PathVariable Long id, @PathVariable Long asigId) {
        asignacionService.delete(asigId);
        return "redirect:/admin/partidos/" + id + "/asignaciones";
    }
}

