package com.kalkulator.parser;

public class BinaryOperatorNode implements ExpressionNode {
    private final ExpressionNode left;
    private final ExpressionNode right;
    private final char operator;

    public BinaryOperatorNode(ExpressionNode left, ExpressionNode right, char operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public double evaluate() {
        double l = left.evaluate();
        double r = right.evaluate();
        return switch (operator) {
            case '+' -> l + r;
            case '-' -> l - r;
            case '*' -> l * r;
            case '/' -> l / r;
            case '^' -> Math.pow(l, r);
            default -> throw new RuntimeException("Nieznany operator: " + operator);
        };
    }
}
