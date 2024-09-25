package com.vann.exceptions;

import java.util.List;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BulkOperationException.class)
    public ResponseEntity<List<String>> handleBulkOperationException(BulkOperationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrorMessages());
    }

    @ExceptionHandler(FieldConflictException.class)
    public ResponseEntity<String> handleFieldConflictException(FieldConflictException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<String> handleRecordNotFoundException(RecordNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

}
