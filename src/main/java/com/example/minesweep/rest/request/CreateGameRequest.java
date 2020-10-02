package com.example.minesweep.rest.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateGameRequest {

    @NotNull
    @Max(200)
    private Integer columns;
    @NotNull
    @Max(200)
    private Integer rows;
    @NotNull
    private Integer mines;
}
