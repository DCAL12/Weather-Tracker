package models;

public class Threshold {
    private Operator operator;
    private String operatorSymbol;
    private float value;

    public enum Operator {
        EQUALS("="),
        NOT_EQUAL("!="),
        GREATER_THAN(">"),
        LESS_THAN("<"),
        GREATER_THAN_OR_EQUAL(">="),
        LESS_THAN_OR_EQUAL("<=");

        private final String symbol;

        Operator(String symbol) {
            this.symbol = symbol;
        }
    }

    public Threshold(Operator operator, float value) {
        this.operator = operator;
        operatorSymbol = operator.symbol;
        this.value = value;
    }

    public Operator getOperator() {
        return operator;
    }

    public float getValue() {
        return value;
    }

    public Boolean isExceeded(float observation) {
        switch (operator) {
            case EQUALS:
                return observation == value;
            case NOT_EQUAL:
                return observation != value;
            case GREATER_THAN:
                return observation > value;
            case LESS_THAN:
                return observation < value;
            case GREATER_THAN_OR_EQUAL:
                return observation >= value;
            case LESS_THAN_OR_EQUAL:
                return  observation <= value;
        }
        return false;
    }

    @Override
    public String toString() {
        return operatorSymbol + " " + value;
    }
}


