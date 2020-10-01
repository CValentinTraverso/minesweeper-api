package com.example.minesweep.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "The resource does not belong to you")
public class ForbiddenException extends RuntimeException {

}
