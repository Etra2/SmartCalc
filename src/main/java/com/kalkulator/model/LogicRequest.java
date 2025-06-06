package com.kalkulator.model;

public class LogicRequest {
    private String expression;

    public LogicRequest() { }

    public LogicRequest(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}

