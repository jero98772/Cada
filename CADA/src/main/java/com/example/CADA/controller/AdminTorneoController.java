package com.example.CADA.controller;

import com.example.CADA.model.Partido;
import com.example.CADA.model.Torneo;
import com.example.CADA.service.PartidoService;
import com.example.CADA.service.TorneoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/torneos")
public class AdminTorneoController {

    private final TorneoService torneoService;
    private final PartidoService partidoService;

    public AdminTorneoController(TorneoService torneoService, PartidoService partidoService) {
        this.torneoService = torneoService;
        this.partidoService = partidoService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("torneos", torneoService.findAll());
        return "admin/torneos/list";
    }

    @GetMapping("/nuevo")
    public String createForm(Model model) {
        model.addAttribute("torneo", new Torneo());
        return "admin/torneos/form";
    }

    @PostMapping
    public String create(@ModelAttribute @Valid Torneo torneo, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/torneos/form";
        }
        torneoService.create(torneo);
        return "redirect:/admin/torneos";
    }

    @GetMapping("/{id}/editar")
    public String editForm(@PathVariable Long id, Model model) {
        Torneo t = torneoService.findById(id).orElseThrow();
        model.addAttribute("torneo", t);
        return "admin/torneos/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute @Valid Torneo torneo, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/torneos/form";
        }
        torneoService.update(id, torneo);
        return "redirect:/admin/torneos";
    }

    @PostMapping("/{id}/toggle-activo")
    public String toggle(@PathVariable Long id) {
        torneoService.toggleActivo(id);
        return "redirect:/admin/torneos";
    }

    @PostMapping("/{id}/eliminar")
    public String delete(@PathVariable Long id) {
        torneoService.delete(id);
        return "redirect:/admin/torneos";
    }

    // Gestión de partidos dentro de un torneo
    @GetMapping("/{id}/partidos")
    public String gestionarPartidos(@PathVariable Long id, Model model) {
        Torneo t = torneoService.findById(id).orElseThrow();
        List<Partido> asignados = partidoService.findByTorneoId(id);
        List<Partido> disponibles = partidoService.findUnassigned();
        model.addAttribute("torneo", t);
        model.addAttribute("asignados", asignados);
        model.addAttribute("disponibles", disponibles);
        return "admin/torneos/partidos";
    }

    @PostMapping("/{id}/partidos/asignar")
    public String asignarPartidos(@PathVariable Long id, @RequestParam(value = "partidoIds", required = false) List<Long> partidoIds) {
        if (partidoIds != null) {
            for (Long pid : partidoIds) {
                Partido p = partidoService.findById(pid).orElse(null);
                if (p != null) {
                    p.setTorneoId(id);
                    partidoService.update(p.getId(), p);
                }
            }
        }
        return "redirect:/admin/torneos/" + id + "/partidos";
    }
}

