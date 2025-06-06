package com.kalkulator.model;

import java.util.List;
import java.util.Map;

public class LogicResult {
    private List<String> variables;
    private List<Map<String, String>> truthTable;

    public LogicResult(List<String> variables, List<Map<String, String>> truthTable) {
        this.variables = variables;
        this.truthTable = truthTable;
    }

    public List<String> getVariables() {
        return variables;
    }

    public void setVariables(List<String> variables) {
        this.variables = variables;
    }

    public List<Map<String, String>> getTruthTable() {
        return truthTable;
    }

    public void setTruthTable(List<Map<String, String>> truthTable) {
        this.truthTable = truthTable;
    }
}
