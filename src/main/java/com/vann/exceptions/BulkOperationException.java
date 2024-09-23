package com.vann.exceptions;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.vann.utils.LogHandler;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BulkOperationException extends RuntimeException {

    private final List<String> errorMessages;

    public BulkOperationException(List<String> errorMessages) {
        super("Bulk operation failed with errors: " + String.join(", ", errorMessages));
        this.errorMessages = errorMessages;
        logErrorMessages(errorMessages);
    }

    private void logErrorMessages(List<String> errorMessages) {
        for (String errorMessage : errorMessages) {
            LogHandler.status400BadRequest(errorMessage);
        }
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

}
