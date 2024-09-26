package com.vann.exceptions;

import java.util.*;
import java.util.regex.*;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


@ControllerAdvice
public class GlobalExceptionHandler {

    private String matchPattern(String rawString) {
        Pattern pattern = Pattern.compile("\\|(\\s(\\s?[0-9a-zA-Z]+)+\\s)\\|\\s([\\S]+)");
        Matcher matcher = pattern.matcher(rawString);
        if (matcher.find()) {
            String coreMessage = matcher.group(1).toUpperCase();  // e.g. "record not found" message
            coreMessage = coreMessage.trim();
            String detail = matcher.group(3);  // e.g. id=123-456 or email=user@example.com

            return coreMessage + ": " + detail;
        }
        return "";
    }

    @ExceptionHandler(BulkOperationException.class)
    public ResponseEntity<List<String>> handleBulkOperationException(BulkOperationException e) {
        List<String> responseMessages = new ArrayList<>();
        for (String errorMessage : e.getErrorMessages()) {
            String responseMessage = matchPattern(errorMessage);
            responseMessages.add(responseMessage);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessages);
    }

    @ExceptionHandler(FieldConflictException.class)
    public ResponseEntity<String> handleFieldConflictException(FieldConflictException e) {
        String responseMessage = matchPattern(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseMessage);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<String> handleRecordNotFoundException(RecordNotFoundException e) {
        String responseMessage = matchPattern(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
    }

}
