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

        System.out.println("Otrzymano matrixA:");
        Arrays.stream(matrixA).forEach(row -> System.out.println(Arrays.toString(row)));
        System.out.println("Otrzymano matrixB:");
        Arrays.stream(matrixB).forEach(row -> System.out.println(Arrays.toString(row)));

        if (matrixA == null || matrixB == null) {
            throw new IllegalArgumentException("Macierze nie mogą być null.");
        }

        return matrixService.add(matrixA, matrixB);
    }

    @PostMapping("/subtract")
    public double[][] subtract(@RequestBody MatrixRequest request) {
        return matrixService.subtract(request.getMatrixA(), request.getMatrixB());
    }

    @PostMapping("/multiplyScalar")
    public double[][] multiplyScalar(@RequestBody MatrixRequest request, @RequestParam double scalar) {
        return matrixService.multiplyByScalar(request.getMatrixA(), scalar);
    }

    @PostMapping("/multiplyMatrix")
    public double[][] multiplyMatrix(@RequestBody MatrixRequest request) {
        return matrixService.multiplyMatrices(request.getMatrixA(), request.getMatrixB());
    }

    @PostMapping("/transpose")
    public double[][] transpose(@RequestBody MatrixRequest request) {
        return matrixService.transpose(request.getMatrixA());
    }

    @PostMapping("/power")
    public double[][] power(@RequestBody MatrixRequest request) {
        return matrixService.power(request.getMatrixA(), request.getExponent());
    }

    @PostMapping("/inverse")
    public double[][] inverse(@RequestBody MatrixRequest request) {
        return matrixService.inverse(request.getMatrixA());
    }

    @PostMapping("/rank")
    public int rank(@RequestBody MatrixRequest request) {
        return matrixService.rank(request.getMatrixA());
    }
}
