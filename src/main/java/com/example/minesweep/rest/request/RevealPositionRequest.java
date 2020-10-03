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
public class RevealPositionRequest {

    @NotNull
    @ApiModelProperty("Column to reveal")
    private Integer column;
    @NotNull
    @ApiModelProperty("Field to reveal")
    private Integer field;
}
