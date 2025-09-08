package com.example.CADA.controller;

import com.example.CADA.model.Torneo;
import com.example.CADA.service.TorneoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/torneos")
public class AdminTorneoController {

    private final TorneoService torneoService;

    public AdminTorneoController(TorneoService torneoService) {
        this.torneoService = torneoService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("torneos", torneoService.findAll());
        return "torneos/list";
    }

    @GetMapping("/nuevo")
    public String createForm(Model model) {
        model.addAttribute("torneo", new Torneo());
        return "torneos/form";
    }

    @PostMapping
    public String create(@ModelAttribute @Valid Torneo torneo, BindingResult result) {
        if (result.hasErrors()) {
            return "torneos/form";
        }
        torneoService.create(torneo);
        return "redirect:/torneos";
    }

    @GetMapping("/{id}/editar")
    public String editForm(@PathVariable Long id, Model model) {
        Torneo t = torneoService.findById(id).orElseThrow();
        model.addAttribute("torneo", t);
        return "torneos/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute @Valid Torneo torneo, BindingResult result) {
        if (result.hasErrors()) {
            return "torneos/form";
        }
        torneoService.update(id, torneo);
        return "redirect:/torneos";
    }

    @PostMapping("/{id}/toggle-activo")
    public String toggle(@PathVariable Long id) {
        torneoService.toggleActivo(id);
        return "redirect:/torneos";
    }

    @PostMapping("/{id}/eliminar")
    public String delete(@PathVariable Long id) {
        torneoService.delete(id);
        return "redirect:/torneos";
    }
}

