package com.kalkulator.parser;

import java.util.List;

public class FunctionNode implements ExpressionNode {
    private final String functionName;
    private final List<ExpressionNode> arguments;

    public FunctionNode(String functionName, List<ExpressionNode> arguments) {
        this.functionName = functionName;
        this.arguments = arguments;
    }

    @Override
    public double evaluate() {
        // Obsługuje różne funkcje matematyczne
        switch (functionName) {
            case "sin" -> {
                if (arguments.size() != 1) throw new RuntimeException("Sin wymaga 1 argumentu");
                return Math.sin(arguments.get(0).evaluate());
            }
            case "cos" -> {
                if (arguments.size() != 1) throw new RuntimeException("Cos wymaga 1 argumentu");
                return Math.cos(arguments.get(0).evaluate());
            }
            case "tan" -> {
                if (arguments.size() != 1) throw new RuntimeException("Tan wymaga 1 argumentu");
                return Math.tan(arguments.get(0).evaluate());
            }
            case "ln" -> {
                if (arguments.size() != 1) throw new RuntimeException("Ln wymaga 1 argumentu");
                return Math.log(arguments.get(0).evaluate());
            }
            case "log" -> {
                if (arguments.size() != 1) throw new RuntimeException("Log wymaga 1 argumentu");
                return Math.log10(arguments.get(0).evaluate());
            }
            case "abs" -> {
                if (arguments.size() != 1) throw new RuntimeException("Abs wymaga 1 argumentu");
                return Math.abs(arguments.get(0).evaluate());
            }
            case "sqrt" -> {
                if (arguments.size() != 1) throw new RuntimeException("Sqrt wymaga 1 argumentu");
                return Math.sqrt(arguments.get(0).evaluate());
            }
            case "silnia" -> {
                if (arguments.size() != 1) throw new RuntimeException("Silnia wymaga 1 argumentu");
                return factorial(arguments.get(0).evaluate());
            }
            case "rad" -> {
                if (arguments.size() != 1) throw new RuntimeException("Rad wymaga 1 argumentu");
                return arguments.get(0).evaluate() * Math.PI / 180;
            }
            case "root" -> {
                if (arguments.size() != 2) throw new RuntimeException("Root wymaga 2 argumentów");
                return Math.pow(arguments.get(1).evaluate(), 1.0 / arguments.get(0).evaluate());
            }
            default -> throw new RuntimeException("Nieznana funkcja: " + functionName);
        }
    }

    private double factorial(double n) {
        if (n < 0) throw new RuntimeException("Silnia z liczby ujemnej");
        if (n == 0 || n == 1) return 1;
        double result = 1;
        for (int i = 2; i <= (int) n; i++) {
            result *= i;
        }
        return result;
    }
}
