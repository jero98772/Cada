
package com.example.CADA.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AdminHomeController {

    // Test endpoint to verify controller works
    @GetMapping("/admin/test")
    @ResponseBody
    public String test() {
        return "Controller is working!";
    }

    // Original endpoint
    @GetMapping("/admin")
    public String index() {
        return "index";
    }
}