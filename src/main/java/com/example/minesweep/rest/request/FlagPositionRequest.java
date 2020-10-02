package com.example.minesweep.rest.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FlagPositionRequest {

    @NotNull
    private Integer column;
    @NotNull
    private Integer field;
    @NotNull
    private FlagType flagType;
}
