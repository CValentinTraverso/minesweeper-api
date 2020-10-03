package com.example.minesweep.rest.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateSessionRequest {

    @NotNull
    @Email
    @ApiModelProperty("Valid email of the account owner")
    private String email;
    @NotBlank
    @ApiModelProperty("Account password")
    private String password;
}
