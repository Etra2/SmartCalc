package com.kalkulator.controller;

import com.kalkulator.parser.ExpressionNode;
import com.kalkulator.parser.Parser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/scientific")
public class ScientificCalculatorController {

    private final Parser parser;

    // Konstruktor wstrzykujący parser
    public ScientificCalculatorController(Parser parser) {
        this.parser = parser;
    }

    @GetMapping
    public String showScientificCalculator(Model model) {
        model.addAttribute("activeTab", "scientific");
        model.addAttribute("expression", "");
        model.addAttribute("result", "");
        return "scientific";
    }

    @PostMapping("/calculate")
    public String calculate(@RequestParam String expression, Model model) {
        String result;
        try {
            // Parsowanie wyrażenia
            ExpressionNode parsedExpression = parser.parse(expression);

            // Obliczanie wyniku
            double evaluationResult = parsedExpression.evaluate();
            result = String.valueOf(evaluationResult);
        } catch (Exception e) {
            result = "Błąd: " + e.getMessage();
        }

        model.addAttribute("expression", expression);
        model.addAttribute("result", result);
        model.addAttribute("activeTab", "scientific"); // <-- DODAJ TO
        return "scientific";
    }
}
