package com.example.minesweep.rest.request;

import com.example.minesweep.exception.BadRequestException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Optional;

public enum FlagType {
    FLAG(1),
    QUESTION_MARK(2);

    int code;

    FlagType(int code) {
        this.code = code;
    }

    @JsonCreator
    public static FlagType of(Integer code) {
        Optional<FlagType> first = Arrays.stream(FlagType.values()).filter(f -> f.getCode() == code).findFirst();
        if (!first.isPresent()) {
            throw new BadRequestException();
        }
        return first.get();
    }

    @JsonValue
    public int getCode() {
        return code;
    }
}
