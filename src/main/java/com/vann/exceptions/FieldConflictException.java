package com.vann.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.vann.utils.LogHandler;


@ResponseStatus(HttpStatus.CONFLICT)
public class FieldConflictException extends RuntimeException {

    public FieldConflictException(String message) {
        super(message);
        LogHandler.status409Conflict(message);
    }

}
