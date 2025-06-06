package com.kalkulator.controller;

import com.kalkulator.service.OpenAiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiController {

    private final OpenAiService openAiService;

    public AiController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping("/solve")
    public Mono<String> solve(@RequestBody Map<String, String> request) {
        String task = request.get("task");
        String option = request.get("option");

        if (task == null || task.isBlank()) {
            return Mono.just("Błąd: Treść zadania jest wymagana.");
        }

        if (option == null || option.isBlank()) {
            option = "rozwiazanie"; // Domyślna opcja, jeśli nie podano
        }

        String prompt = switch (option) {
            case "wzory" -> "Podaj wzory potrzebne do rozwiązania zadania: " + task;
            case "wynik" -> "Podaj tylko wynik zadania: " + task;
            case "rozwiazanie" -> "Podaj pełne rozwiązanie zadania krok po kroku: " + task;
            default -> "Podaj rozwiązanie zadania: " + task;
        };

        return openAiService.getChatCompletion(prompt)
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just("Wystąpił błąd podczas przetwarzania zapytania: " + e.getMessage());
                });
    }
}
