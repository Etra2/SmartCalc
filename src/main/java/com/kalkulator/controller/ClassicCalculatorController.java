package com.kalkulator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/classic")
public class ClassicCalculatorController {

    @GetMapping
    public String showCalculator(Model model) {
        // Dodanie zmiennej, która kontroluje, która zakładka jest aktywna
        model.addAttribute("activeTab", "classic");
        return "classic";
    }

    @PostMapping("/calculate")
    public String calculate(@RequestParam String expression, Model model) {
        if (expression.isEmpty()) {
            model.addAttribute("result", "Wprowadź wyrażenie");
            model.addAttribute("activeTab", "classic");
            return "classic";
        }

        double result;
        try {
            result = eval(expression); // tymczasowy parser
        } catch (Exception e) {
            model.addAttribute("result", "Błąd składni");
            model.addAttribute("expression", expression);
            model.addAttribute("activeTab", "classic");
            return "classic";
        }

        model.addAttribute("expression", expression);
        model.addAttribute("result", result);
        model.addAttribute("activeTab", "classic");
        return "classic";
    }

    // Tymczasowy parser do obliczania wyrażeń (potem zmienić na własny parser)
    private double eval(String expr) {
        return new net.objecthunter.exp4j.ExpressionBuilder(expr).build().evaluate();
    }
}
