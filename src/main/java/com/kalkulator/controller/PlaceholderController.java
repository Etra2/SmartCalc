package com.kalkulator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PlaceholderController {

    @GetMapping("/scientific")
    public String scientific(Model model) {
        model.addAttribute("activeTab", "scientific");
        model.addAttribute("message", "Rozwiązanie w budowie");
        return "scientific";
    }

    @GetMapping("/matrix")
    public String matrix(Model model) {
        model.addAttribute("activeTab", "matrix");
        model.addAttribute("message", "Rozwiązanie w budowie");
        return "matrix";
    }

    @GetMapping("/logic")
    public String logic(Model model) {
        model.addAttribute("activeTab", "logic");
        model.addAttribute("message", "Rozwiązanie w budowie");
        return "logic";
    }

    @GetMapping("/ai")
    public String ai(Model model) {
        model.addAttribute("activeTab", "ai");
        model.addAttribute("message", "Rozwiązanie w budowie");
        return "ai";
    }
}
