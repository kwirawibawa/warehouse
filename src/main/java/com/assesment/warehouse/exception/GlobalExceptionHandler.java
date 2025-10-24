package com.assesment.warehouse.exception;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.assesment.warehouse.model.response.BaseResponse;
import com.assesment.warehouse.util.CommonConstants;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotEnoughStockException.class)
    public ResponseEntity<Map<String, Object>> handleNotEnough(NotEnoughStockException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.error(CommonConstants.BAD_REQUEST_CODE, ex.getMessage()).toMap());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.error(CommonConstants.NOT_FOUND_CODE, ex.getMessage()).toMap());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder message = new StringBuilder("Validation failed: ");
        ex.getBindingResult().getFieldErrors().forEach(error -> message.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append("; "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.error(CommonConstants.BAD_REQUEST_CODE, message.toString()).toMap());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("Unhandled exception occurred: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.error(CommonConstants.INTERNAL_ERROR_CODE, "Internal server error").toMap());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(BaseResponse.error(
                        String.valueOf(ex.getStatusCode().value()),
                        ex.getReason()
                ).toMap());
    }
}