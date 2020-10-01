package com.example.minesweep.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Username and/or password is incorrect")
public class InvalidCredentialsException extends RuntimeException {


}
