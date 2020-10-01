package com.example.minesweep.rest.request;

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
public class CreateAccountRequest {

    @NotNull
    @Email
    private String email;
    @NotBlank
    private String name;
    @NotBlank
    private String password;
}
