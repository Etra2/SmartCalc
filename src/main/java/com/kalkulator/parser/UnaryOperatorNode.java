package com.kalkulator.parser;

public class UnaryOperatorNode implements ExpressionNode{
    private final char operator;
    private final ExpressionNode operand;

    public UnaryOperatorNode(char operator, ExpressionNode operand) {
        this.operator = operator;
        this.operand = operand;
    }

    @Override
    public double evaluate() {
        double value = operand.evaluate();
        return switch (operator) {
            case '+' -> value;
            case '-' -> -value;
            default -> throw new RuntimeException("Nieznany operator unarny " + operator);
        };
    }
}
