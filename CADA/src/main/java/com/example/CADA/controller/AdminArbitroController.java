package com.example.CADA.controller;

import com.example.CADA.model.Arbitro;
import com.example.CADA.model.Escalafon;
import com.example.CADA.model.Especialidad;
import com.example.CADA.service.ArbitroService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/arbitros")
public class AdminArbitroController {

    private final ArbitroService arbitroService;

    public AdminArbitroController(ArbitroService arbitroService) {
        this.arbitroService = arbitroService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("arbitros", arbitroService.findAll());
        return "arbitros/list";
    }

    @GetMapping("/nuevo")
    public String createForm(Model model) {
        model.addAttribute("arbitro", new Arbitro());
        model.addAttribute("especialidades", Especialidad.values());
        model.addAttribute("escalafones", Escalafon.values());
        return "arbitros/form";
    }

    @PostMapping
    public String create(@ModelAttribute @Valid Arbitro arbitro, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("especialidades", Especialidad.values());
            model.addAttribute("escalafones", Escalafon.values());
            return "arbitros/form";
        }
        if (arbitro.getUsername() == null || arbitro.getUsername().isBlank()) {
            // por simplicidad, usar email como username si no viene
            arbitro.setUsername(arbitro.getEmail());
        }
        arbitroService.create(arbitro);
        return "redirect:/admin/arbitros";
    }

    @GetMapping("/{id}/editar")
    public String editForm(@PathVariable Long id, Model model) {
        Arbitro a = arbitroService.findById(id).orElseThrow();
        model.addAttribute("arbitro", a);
        model.addAttribute("especialidades", Especialidad.values());
        model.addAttribute("escalafones", Escalafon.values());
        return "admin/arbitros/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute @Valid Arbitro arbitro, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("especialidades", Especialidad.values());
            model.addAttribute("escalafones", Escalafon.values());
            return "admin/arbitros/form";
        }
        arbitroService.update(id, arbitro);
        return "redirect:/admin/arbitros";
    }

    @PostMapping("/{id}/toggle-activo")
    public String toggle(@PathVariable Long id) {
        arbitroService.toggleActivo(id);
        return "redirect:/admin/arbitros";
    }

    @PostMapping("/{id}/eliminar")
    public String delete(@PathVariable Long id) {
        arbitroService.delete(id);
        return "redirect:/admin/arbitros";
    }
}

