package com.kalkulator.service;

import org.springframework.stereotype.Service;

@Service
public class MatrixService {

    // Dodawanie dwóch macierzy o tych samych wymiarach
    public double[][] add(double[][] a, double[][] b) {
        validateSameDimensions(a, b);

        int rows = a.length;
        int cols = a[0].length;
        double[][] result = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = a[i][j] + b[i][j];
            }
        }

        return result;
    }

    // Odejmowanie dwóch macierzy o tych samych wymiarach
    public double[][] subtract(double[][] a, double[][] b) {
        validateSameDimensions(a, b);

        int rows = a.length;
        int cols = a[0].length;
        double[][] result = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = a[i][j] - b[i][j];
            }
        }

        return result;
    }

    // Mnożenie macierzy przez skalar (liczbę)
    public double[][] multiplyByScalar(double[][] matrix, double scalar) {
        validateMatrix(matrix);

        int rows = matrix.length;
        int cols = matrix[0].length;
        double[][] result = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = matrix[i][j] * scalar;
            }
        }

        return result;
    }

    // Mnożenie dwóch macierzy (a: m x n, b: n x p → wynik: m x p)
    public double[][] multiplyMatrices(double[][] a, double[][] b) {
        validateMatrix(a);
        validateMatrix(b);

        if (a[0].length != b.length)
            throw new IllegalArgumentException("Nieprawidłowe wymiary do mnożenia: liczba kolumn A musi być równa liczbie wierszy B.");

        int rows = a.length;
        int cols = b[0].length;
        int common = a[0].length;

        double[][] result = new double[rows][cols];

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                for (int k = 0; k < common; k++)
                    result[i][j] += a[i][k] * b[k][j];

        return result;
    }

    // Transpozycja macierzy (zamiana wierszy z kolumnami)
    public double[][] transpose(double[][] matrix) {
        validateMatrix(matrix);

        int rows = matrix.length;
        int cols = matrix[0].length;
        double[][] result = new double[cols][rows];

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                result[j][i] = matrix[i][j];

        return result;
    }

    // Potęgowanie macierzy (tylko kwadratowych)
    public double[][] power(double[][] matrix, int exponent) {
        validateMatrix(matrix);
        if (matrix.length != matrix[0].length)
            throw new IllegalArgumentException("Macierz musi być kwadratowa do potęgowania.");

        if (exponent < 0)
            throw new IllegalArgumentException("Wykładnik potęgi nie może być ujemny.");

        double[][] result = identityMatrix(matrix.length);
        double[][] base = matrix;

        for (int i = 0; i < exponent; i++) {
            result = multiplyMatrices(result, base);
        }

        return result;
    }

    // Obliczanie macierzy odwrotnej metodą Gaussa-Jordana
    public double[][] inverse(double[][] matrix) {
        validateMatrix(matrix);

        int n = matrix.length;
        if (n != matrix[0].length) {
            throw new IllegalArgumentException("Macierz musi być kwadratowa, aby obliczyć odwrotność.");
        }

        // Tworze macierz rozszerzoną (n x 2n) z macierzą jednostkową po prawej stronie
        double[][] augmented = new double[n][2 * n];
        for (int i = 0; i < n; i++) {
            if (matrix[i].length != n)
                throw new IllegalArgumentException("Macierz musi być kwadratowa.");
            for (int j = 0; j < n; j++) {
                augmented[i][j] = matrix[i][j];
            }
            augmented[i][n + i] = 1.0;
        }

        // Metoda Gaussa-Jordana do obliczenia macierzy odwrotnej
        for (int i = 0; i < n; i++) {
            // Znajduje wiersz z maksymalnym elementem w kolumnie i (pivot)
            int maxRow = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(augmented[k][i]) > Math.abs(augmented[maxRow][i])) {
                    maxRow = k;
                }
            }

            // Zamieniam wiersze i maxRow
            double[] temp = augmented[i];
            augmented[i] = augmented[maxRow];
            augmented[maxRow] = temp;

            // Sprawdzam czy pivot jest różny od zera (macierz odwracalna)
            if (Math.abs(augmented[i][i]) < 1e-10) {
                throw new ArithmeticException("Macierz nie jest odwracalna.");
            }

            // Normalizuje wiersz i
            double pivot = augmented[i][i];
            for (int j = 0; j < 2 * n; j++) {
                augmented[i][j] /= pivot;
            }

            // Eliminuje inne wiersze w kolumnie i
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = augmented[k][i];
                    for (int j = 0; j < 2 * n; j++) {
                        augmented[k][j] -= factor * augmented[i][j];
                    }
                }
            }
        }

        // Wyciągam macierz odwrotną z rozszerzonej macierzy
        double[][] inverse = new double[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(augmented[i], n, inverse[i], 0, n);
        }

        return inverse;
    }

    //rząd macierzy
    public int rank(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        // Kopia macierzy, aby nie modyfikować oryginału
        double[][] mat = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(matrix[i], 0, mat[i], 0, cols);
        }

        int rank = 0;
        int row = 0;

        for (int col = 0; col < cols && row < rows; col++) {
            // Znajduję wiersz z maksymalnym elementem w kolumnie 'col' zaczynając od 'row'
            int maxRow = row;
            for (int r = row + 1; r < rows; r++) {
                if (Math.abs(mat[r][col]) > Math.abs(mat[maxRow][col])) {
                    maxRow = r;
                }
            }

            if (Math.abs(mat[maxRow][col]) < 1e-10) {  // element jest prawie zerem, pomjam kolumnę
                continue;
            }

            // Zamieniam wiersz 'row' z 'maxRow'
            double[] temp = mat[row];
            mat[row] = mat[maxRow];
            mat[maxRow] = temp;

            // Normalizuje wiersz 'row' dzieląc przez element mat[row][col]
            double divisor = mat[row][col];
            for (int c = col; c < cols; c++) {
                mat[row][c] /= divisor;
            }

            // Zeruję kolumnę 'col' poniżej wiersza 'row'
            for (int r = row + 1; r < rows; r++) {
                double factor = mat[r][col];
                for (int c = col; c < cols; c++) {
                    mat[r][c] -= factor * mat[row][c];
                }
            }

            row++;
        }

        // Po przeprowadzeniu eliminacji Gaussa liczę liczbę niezerowych wierszy (rząd)
        rank = 0;
        for (int r = 0; r < rows; r++) {
            boolean nonZeroRow = false;
            for (int c = 0; c < cols; c++) {
                if (Math.abs(mat[r][c]) > 1e-10) {
                    nonZeroRow = true;
                    break;
                }
            }
            if (nonZeroRow) rank++;
        }

        return rank;
    }


    // Pomocnicza metoda: sprawdza czy macierze mają te same wymiary
    private void validateSameDimensions(double[][] a, double[][] b) {
        if (a == null || b == null)
            throw new IllegalArgumentException("Jedna z macierzy jest pusta.");
        if (a.length != b.length || a[0].length != b[0].length)
            throw new IllegalArgumentException("Macierze muszą mieć te same wymiary.");

        for (int i = 0; i < a.length; i++) {
            if (a[i].length != a[0].length || b[i].length != b[0].length)
                throw new IllegalArgumentException("Wszystkie wiersze muszą mieć taką samą liczbę kolumn.");
        }
    }

    // Pomocnicza metoda: sprawdza czy macierz nie jest null i czy wszystkie wiersze mają jednakową długość
    private void validateMatrix(double[][] matrix) {
        if (matrix == null)
            throw new IllegalArgumentException("Macierz jest pusta.");
        int cols = matrix[0].length;
        for (int i = 1; i < matrix.length; i++) {
            if (matrix[i].length != cols) {
                throw new IllegalArgumentException("Wszystkie wiersze macierzy muszą mieć tę samą liczbę kolumn.");
            }
        }
    }

    // Pomocnicza metoda: tworzy macierz jednostkową o zadanym rozmiarze
    private double[][] identityMatrix(int size) {
        double[][] identity = new double[size][size];
        for (int i = 0; i < size; i++) {
            identity[i][i] = 1.0;
        }
        return identity;
    }
}
