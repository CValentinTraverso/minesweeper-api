package com.example.minesweep.rest.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ModifyAccountRequest {

    @ApiModelProperty("New display name for the account, leave it null to not do any changes")
    private String name;
    @NotBlank
    @ApiModelProperty("Current account password")
    private String password;
    @ApiModelProperty("New account password")
    private String newPassword;
}
