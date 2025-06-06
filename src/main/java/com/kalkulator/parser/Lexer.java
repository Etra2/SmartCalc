package com.kalkulator.parser;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String input;
    private int position = 0;

    public Lexer(String input) {
        this.input = input;
    }

    // Zwraca listę tokenów
    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        Token token;
        while ((token = nextToken()).getType() != TokenType.EOF) {
            tokens.add(token);
        }
        tokens.add(token); // dodaj EOF na końcu
        return tokens;
    }

    public Token nextToken() {
        if (position >= input.length()) {
            return new Token(TokenType.EOF, "");
        }

        char currentChar = input.charAt(position);

        // Pomijanie białych znaków
        while (Character.isWhitespace(currentChar)) {
            position++;
            if (position >= input.length()) {
                return new Token(TokenType.EOF, "");
            }
            currentChar = input.charAt(position);
        }

        // Rozpoznawanie liczb
        if (Character.isDigit(currentChar) || currentChar == '.') {
            StringBuilder number = new StringBuilder();
            while (position < input.length() &&
                    (Character.isDigit(currentChar) || currentChar == '.')) {
                number.append(currentChar);
                position++;
                if (position < input.length()) {
                    currentChar = input.charAt(position);
                }
            }
            return new Token(TokenType.NUMBER, number.toString());
        }

        // Rozpoznawanie operatorów i nawiasów
        switch (currentChar) {
            case '+':
                position++;
                return new Token(TokenType.PLUS, "+");
            case '-':
                position++;
                return new Token(TokenType.MINUS, "-");
            case '*':
                position++;
                return new Token(TokenType.MULTIPLY, "*");
            case '/':
                position++;
                return new Token(TokenType.DIVIDE, "/");
            case '^':
                position++;
                return new Token(TokenType.POWER, "^");
            case '(':
                position++;
                return new Token(TokenType.LPAREN, "(");
            case ')':
                position++;
                return new Token(TokenType.RPAREN, ")");
            case ',':
                position++;
                return new Token(TokenType.COMMA, ",");
        }

        // Rozpoznawanie identyfikatorów (funkcje, zmienne)
        if (Character.isAlphabetic(currentChar)) {
            StringBuilder identifier = new StringBuilder();
            while (position < input.length() &&
                    (Character.isAlphabetic(currentChar))) {
                identifier.append(currentChar);
                position++;
                if (position < input.length()) {
                    currentChar = input.charAt(position);
                }
            }
            return new Token(TokenType.IDENTIFIER, identifier.toString().toLowerCase());
        }

        throw new RuntimeException("Nieoczekiwany znak: " + currentChar);
    }
}
