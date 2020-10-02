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
public class CreateGameRequest {

    @NotNull
    private Integer columns;
    @NotNull
    private Integer rows;
    @NotNull
    private Integer mines;
}
