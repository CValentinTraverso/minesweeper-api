package com.example.minesweep.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Sale does not exist")
public class NotFoundException extends RuntimeException {

}
