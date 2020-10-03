package com.example.minesweep.rest.request;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty("Number of columns")
    private Integer columns;
    @NotNull
    @Max(200)
    @ApiModelProperty("Number of rows")
    private Integer rows;
    @NotNull
    @ApiModelProperty("Number of mines, there can't be more mines than available fields")
    private Integer mines;
}
