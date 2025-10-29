package com.ing.assessment.mortgage.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gender options for Mortgage-Check users ("M", "F", "O").
 */
public enum Gender {
    MALE("M"),
    FEMALE("F"),
    OTHER("O");

    private final String code;

    Gender(String code) { this.code = code; }

    @JsonValue
    public String getCode() { return code; }

    @JsonCreator
    public static Gender fromCode(String code) {
        for (Gender g : values()) {
            if (g.code.equalsIgnoreCase(code)) return g;
        }
        throw new IllegalArgumentException("Invalid gender code: " + code);
    }
}
