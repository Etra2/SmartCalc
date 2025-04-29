package com.kalkulator.parser;

public enum TokenType {
    NUMBER,
    PLUS,       // Dodanie + dla operacji dodawania
    MINUS,      // Dodanie - dla operacji odejmowania
    MULTIPLY,   // Dodanie * dla mnożenia
    DIVIDE,     // Dodanie / dla dzielenia
    POWER,      // Dodanie ^ dla potęgowania
    LPAREN,     // (
    RPAREN,     // )
    IDENTIFIER, // zmienne / funkcje
    COMMA,      // ,
    EOF,         // koniec pliku
    //FUNCTION,      // dla funkcji (np. sin, cos, log, abs)
    //VARIABLE,      // dla zmiennych (np. pi, e)
    //OPERATOR      // dla operatorów (+, -, *, /)
}
