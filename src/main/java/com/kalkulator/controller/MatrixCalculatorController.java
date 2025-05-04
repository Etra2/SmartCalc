package com.kalkulator.controller;

import com.kalkulator.model.MatrixRequest;
import com.kalkulator.service.MatrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/matrix")
@CrossOrigin // ważne przy frontendzie na innym porcie
public class MatrixCalculatorController {

    private final MatrixService matrixService;

    @Autowired
    public MatrixCalculatorController(MatrixService matrixService) {
        this.matrixService = matrixService;
    }

    @PostMapping("/add")
    public double[][] add(@RequestBody MatrixRequest request) {
        double[][] matrixA = request.getMatrixA();
        double[][] matrixB = request.getMatrixB();

        // Debug log - można zamienić na logger w przyszłości
        System.out.println("Otrzymano matrixA:");
        Arrays.stream(matrixA).forEach(row -> System.out.println(Arrays.toString(row)));
        System.out.println("Otrzymano matrixB:");
        Arrays.stream(matrixB).forEach(row -> System.out.println(Arrays.toString(row)));

        // Walidacja prostsza i bardziej jednoznaczna
        if (matrixA == null || matrixB == null) {
            throw new IllegalArgumentException("Macierze nie mogą być null.");
        }

        return matrixService.add(matrixA, matrixB);
    }
}


// Dodaj inne endpointy: subtract, multiply, transpose, etc.


