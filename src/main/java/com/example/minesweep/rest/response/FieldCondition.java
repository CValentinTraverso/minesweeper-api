package com.example.minesweep.rest.response;

public enum FieldCondition {
    MINE(1, "mine"),
    HIDDEN(2, "hidden"),
    FLAG(3, "flag"),
    MARKED(4, "marked"),
    REVEALED(5, "revealed");

    Integer code;
    String name;

    FieldCondition(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
