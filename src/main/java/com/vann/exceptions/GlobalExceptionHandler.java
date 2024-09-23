package com.vann.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BulkOperationException.class)
    public ResponseEntity<?> handleBulkOperationException(BulkOperationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrorMessages());
    }

    @ExceptionHandler(FieldConflictException.class)
    public ResponseEntity<?> handleFieldConflictException(FieldConflictException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<?> handleRecordNotFoundException(RecordNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

}
