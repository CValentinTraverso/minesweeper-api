package com.example.minesweep.rest.request;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty("Column to flag")
    private Integer column;
    @NotNull
    @ApiModelProperty("Field to flag")
    private Integer field;
    @NotNull
    @ApiModelProperty("Flag type, 1 for Flag and 2 for Question mark")
    private FlagType flagType;
}
