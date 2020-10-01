package com.example.minesweep.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "An account with that email does not exists")
public class EmailDoesntExistsException extends RuntimeException {

}
