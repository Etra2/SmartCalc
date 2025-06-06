package com.kalkulator.model;

public class MatrixRequest {
    private double[][] matrixA;
    private double[][] matrixB;
    private double scalar;
    private int exponent;

    public double[][] getMatrixA() {
        return matrixA;
    }

    public void setMatrixA(double[][] matrixA) {
        this.matrixA = matrixA;
    }

    public double[][] getMatrixB() {
        return matrixB;
    }

    public void setMatrixB(double[][] matrixB) {
        this.matrixB = matrixB;
    }

    public double getScalar() {
        return scalar;
    }

    public void setScalar(double scalar) {
        this.scalar = scalar;
    }

    public int getExponent() {
        return exponent;
    }

    public void setExponent(int exponent) {
        this.exponent = exponent;
    }
}
