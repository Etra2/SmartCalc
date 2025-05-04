package com.kalkulator.service;

import org.springframework.stereotype.Service;

@Service
public class MatrixService {

    public double[][] add(double[][] a, double[][] b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Jedna z macierzy jest pusta.");
        }

        int rows = a.length;
        int cols = a[0].length;

        if (b.length != rows || b[0].length != cols) {
            throw new IllegalArgumentException("Macierze muszą mieć te same wymiary.");
        }

        double[][] result = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            if (a[i].length != cols || b[i].length != cols) {
                throw new IllegalArgumentException("Wszystkie wiersze muszą mieć tę samą liczbę kolumn.");
            }

            for (int j = 0; j < cols; j++) {
                result[i][j] = a[i][j] + b[i][j];
            }
        }

        return result;
    }

    // inne operacje np. mnożenie, transpozycja, itp.
}
