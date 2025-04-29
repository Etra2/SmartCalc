package com.kalkulator.parser;

public class VariableNode implements ExpressionNode {
    private final String name;

    public VariableNode(String name) {
        this.name = name;
    }

    @Override
    public double evaluate() {
        return switch (name) {
            case "pi" -> Math.PI;
            case "e" -> Math.E;
            default -> throw new RuntimeException("Nieznana zmienna: " + name);
        };
    }
}
