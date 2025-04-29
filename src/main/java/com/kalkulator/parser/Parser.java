package com.kalkulator.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Parser analizuje wyrażenie matematyczne i przekształca je w drzewo wyrażeń (AST).
 */
public class Parser {
    private Lexer lexer;
    private Token currentToken;

    /**
     * Rozpoczyna parsowanie wyrażenia.
     * @param expression String zawierający wyrażenie matematyczne
     * @return korzeń drzewa wyrażeń (AST)
     */
    public ExpressionNode parse(String expression) {
        this.lexer = new Lexer(expression);
        this.currentToken = lexer.nextToken();
        return parseExpression();
    }

    /**
     * Konsumuje token, jeśli zgadza się z oczekiwanym typem.
     */
    private void eat(TokenType expected) {
        if (currentToken.getType() == expected) {
            currentToken = lexer.nextToken();
        } else {
            throw new RuntimeException("Oczekiwano " + expected + ", ale znaleziono " + currentToken.getType());
        }
    }

    /**
     * expression -> term ((+|-) term)*
     */
    private ExpressionNode parseExpression() {
        ExpressionNode node = parseTerm();

        while (currentToken.getType() == TokenType.PLUS || currentToken.getType() == TokenType.MINUS) {
            char operator = currentToken.getValue().charAt(0);
            eat(currentToken.getType());
            ExpressionNode right = parseTerm();
            node = new BinaryOperatorNode(node, right, operator);
        }

        return node;
    }

    /**
     * term -> factor ((*|/) factor)*
     */
    private ExpressionNode parseTerm() {
        ExpressionNode node = parseFactor();

        while (currentToken.getType() == TokenType.MULTIPLY || currentToken.getType() == TokenType.DIVIDE) {
            char operator = currentToken.getValue().charAt(0);
            eat(currentToken.getType());
            ExpressionNode right = parseFactor();
            node = new BinaryOperatorNode(node, right, operator);
        }

        return node;
    }

    /**
     * factor -> unary (^ unary)*
     * Obsługuje prawostronną asocjację operatora potęgowania.
     */
    private ExpressionNode parseFactor() {
        ExpressionNode node = parseUnary();

        while (currentToken.getType() == TokenType.POWER) {
            eat(TokenType.POWER);
            ExpressionNode right = parseUnary();
            node = new BinaryOperatorNode(node, right, '^');
        }

        return node;
    }

    /**
     * unary -> (+|-) unary | primary
     */
    private ExpressionNode parseUnary() {
        if (currentToken.getType() == TokenType.PLUS || currentToken.getType() == TokenType.MINUS) {
            char operator = currentToken.getValue().charAt(0);
            eat(currentToken.getType());
            ExpressionNode operand = parseUnary();
            return new UnaryOperatorNode(operator, operand);
        }

        return parsePrimary();
    }

    /**
     * primary -> NUMBER | IDENTIFIER | IDENTIFIER(args) | (expression)
     */
    private ExpressionNode parsePrimary() {
        Token token = currentToken;

        return switch (token.getType()) {
            case NUMBER -> {
                eat(TokenType.NUMBER);
                yield new NumberNode(Double.parseDouble(token.getValue()));
            }
            case IDENTIFIER -> {
                String name = token.getValue();
                eat(TokenType.IDENTIFIER);

                if (currentToken.getType() == TokenType.LPAREN) {
                    eat(TokenType.LPAREN);
                    List<ExpressionNode> args = new ArrayList<>();

                    if (currentToken.getType() != TokenType.RPAREN) {
                        args.add(parseExpression());
                        while (currentToken.getType() == TokenType.COMMA) {
                            eat(TokenType.COMMA);
                            args.add(parseExpression());
                        }
                    }

                    eat(TokenType.RPAREN);
                    yield new FunctionNode(name, args);
                } else {
                    yield new VariableNode(name);
                }
            }
            case LPAREN -> {
                eat(TokenType.LPAREN);
                ExpressionNode expr = parseExpression();
                eat(TokenType.RPAREN);
                yield expr;
            }
            default -> throw new RuntimeException("Nieoczekiwany token: " + token.getType());
        };
    }
}
