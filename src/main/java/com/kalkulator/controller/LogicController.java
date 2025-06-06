package com.kalkulator.controller;

import com.kalkulator.model.LogicRequest;
import com.kalkulator.model.LogicResult;
import com.kalkulator.service.LogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logic")
public class LogicController {

    private final LogicService logicService;

    @Autowired
    public LogicController(LogicService logicService) {
        this.logicService = logicService;
    }

    // Endpoint przyjmujący JSON: { "expression": "r∧s" }
    @PostMapping("/evaluate")
    public ResponseEntity<?> evaluate(@RequestBody LogicRequest request) {
        try {
            LogicResult result = logicService.evaluate(request.getExpression());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Błąd składni wyrażenia: " + e.getMessage());
        }
    }
}