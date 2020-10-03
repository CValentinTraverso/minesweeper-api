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
public class UnflagPositionRequest {

    @NotNull
    @ApiModelProperty("Column to unflag")
    private Integer column;
    @NotNull
    @ApiModelProperty("Field to unflag")
    private Integer field;
}
