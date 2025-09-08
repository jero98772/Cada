package com.example.CADA.controller;

import com.example.CADA.model.Arbitro;
import com.example.CADA.model.Asignacion;
import com.example.CADA.service.ArbitroService;
import com.example.CADA.service.AsignacionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final ArbitroService arbitroService;
    private final AsignacionService asignacionService;

    public DashboardController(ArbitroService arbitroService, AsignacionService asignacionService) {
        this.arbitroService = arbitroService;
        this.asignacionService = asignacionService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        String username = auth != null ? auth.getName() : "test";
        Arbitro arbitro = arbitroService.findByUsername(username).orElse(null);
        List<Asignacion> asignaciones = arbitro == null ? List.of() : asignacionService.findByArbitroId(arbitro.getId());
        model.addAttribute("arbitro", arbitro);
        model.addAttribute("asignaciones", asignaciones);
        return "dashboard"; // not "dashboard/index"
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

