package com.kalkulator.controller;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/scientific")
public class ScientificCalculatorController {

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
            // Zamień "pi" i "e" na wartości z exp4j
            expression = expression.replace("pi", String.valueOf(Math.PI))
                    .replace("e", String.valueOf(Math.E));

            Expression exp = new ExpressionBuilder(expression)
                    .functions(
                            new net.objecthunter.exp4j.function.Function("root", 2) {
                                @Override
                                public double apply(double... args) {
                                    return Math.pow(args[1], 1.0 / args[0]);
                                }
                            },
                            new net.objecthunter.exp4j.function.Function("abs", 1) {
                                @Override
                                public double apply(double... args) {
                                    return Math.abs(args[0]);
                                }
                            },
                            new net.objecthunter.exp4j.function.Function("sinlia", 1) {
                                @Override
                                public double apply(double... args) {
                                    return Math.sin(args[0]) + Math.log(args[0]);
                                }
                            }
                    )
                    .build();
            double evaluationResult = exp.evaluate();
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
