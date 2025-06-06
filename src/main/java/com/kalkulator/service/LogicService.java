package com.kalkulator.service;

import com.kalkulator.model.LogicResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LogicService {

    private static final Map<String, Integer> PRECEDENCE = Map.of(
            "¬", 4,
            "∧", 3,
            "∨", 2,
            "→", 1,
            "↔", 0
    );

    public LogicResult evaluate(String expression) {
        List<String> variables = extractVariables(expression);
        List<List<String>> permutations = generatePermutations(variables.size());
        List<Map<String, String>> truthTable = new ArrayList<>();

        List<String> postfix = infixToPostfix(expression);

        for (List<String> values : permutations) {
            Map<String, String> row = new LinkedHashMap<>();
            for (int i = 0; i < variables.size(); i++) {
                row.put(variables.get(i), values.get(i));
            }
            String result = evaluatePostfix(postfix, variables, values);
            row.put("Wynik", result);
            truthTable.add(row);
        }

        return new LogicResult(variables, truthTable);
    }

    private List<String> extractVariables(String expression) {
        Set<String> vars = new TreeSet<>();
        Matcher matcher = Pattern.compile("[A-Za-z]").matcher(expression);
        while (matcher.find()) {
            vars.add(matcher.group().toUpperCase());
        }
        return new ArrayList<>(vars);
    }

    private List<List<String>> generatePermutations(int n) {
        int rows = (int) Math.pow(2, n);
        List<List<String>> permutations = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            List<String> values = new ArrayList<>();
            for (int j = n - 1; j >= 0; j--) {
                values.add(((i >> j) & 1) == 1 ? "1" : "0");
            }
            permutations.add(values);
        }
        return permutations;
    }

    private List<String> infixToPostfix(String expression) {
        List<String> output = new ArrayList<>();
        Stack<String> stack = new Stack<>();

        for (int i = 0; i < expression.length(); ) {
            char ch = expression.charAt(i);

            if (Character.isWhitespace(ch)) {
                i++;
                continue;
            }

            if (Character.isLetter(ch)) {
                output.add(String.valueOf(Character.toUpperCase(ch)));
                i++;
            } else if (ch == '(') {
                stack.push("(");
                i++;
            } else if (ch == ')') {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    output.add(stack.pop());
                }
                if (!stack.isEmpty() && stack.peek().equals("(")) {
                    stack.pop();
                }
                i++;
            } else {
                String op;
                if ((ch == '→' || ch == '↔') && i + 1 < expression.length()) {
                    // operator → lub ↔ jest jeden znak, nic do zmiany, i++ w dole
                    op = String.valueOf(ch);
                } else {
                    op = String.valueOf(ch);
                }

                while (!stack.isEmpty() && !stack.peek().equals("(") &&
                        PRECEDENCE.getOrDefault(stack.peek(), -1) >= PRECEDENCE.getOrDefault(op, -1)) {
                    output.add(stack.pop());
                }
                stack.push(op);
                i++;
            }
        }

        while (!stack.isEmpty()) {
            output.add(stack.pop());
        }

        return output;
    }

    private String evaluatePostfix(List<String> postfix, List<String> vars, List<String> vals) {
        Stack<String> stack = new Stack<>();
        Map<String, String> varMap = new HashMap<>();
        for (int i = 0; i < vars.size(); i++) {
            varMap.put(vars.get(i), vals.get(i));
        }

        for (String token : postfix) {
            if (token.length() == 1 && Character.isLetter(token.charAt(0))) {
                stack.push(varMap.get(token));
            } else if (token.equals("¬")) {
                String a = stack.pop();
                stack.push(a.equals("1") ? "0" : "1");
            } else {
                // dwie operandy
                String b = stack.pop();
                String a = stack.pop();

                switch (token) {
                    case "∧":
                        stack.push((a.equals("1") && b.equals("1")) ? "1" : "0");
                        break;
                    case "∨":
                        stack.push((a.equals("1") || b.equals("1")) ? "1" : "0");
                        break;
                    case "→":
                        stack.push((a.equals("1") && b.equals("0")) ? "0" : "1");
                        break;
                    case "↔":
                        stack.push(a.equals(b) ? "1" : "0");
                        break;
                    default:
                        throw new IllegalArgumentException("Nieznany operator: " + token);
                }
            }
        }
        return stack.pop();
    }
}
