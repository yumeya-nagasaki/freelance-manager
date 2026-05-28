package com.example.freelancemanager.common;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        
        HttpStatus status = HttpStatus.NOT_FOUND;

        ErrorResponse response = new ErrorResponse(
            status.value(), 
            status.getReasonPhrase(), 
            ex.getMessage(), 
            request.getRequestURI()
        );

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        String message = ex.getBindingResult()
            .getAllErrors()
            .stream()
            .map(this::formatValidationError)
            .collect(Collectors.joining(", "));
        
        ErrorResponse response = new ErrorResponse(
            status.value(), 
            status.getReasonPhrase(), 
            message, 
            request.getRequestURI()
        );

        return ResponseEntity.status(status).body(response);
    }

    private String formatValidationError(ObjectError error) {
        if (error instanceof FieldError fieldError) {
            return fieldError.getField() + ": " + fieldError.getDefaultMessage();
        }
    
        return error.getDefaultMessage();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException ex, 
        HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        String message = "リクエストの形式が不正です";

        if (ex.getCause() instanceof InvalidFormatException invalidFormatException
                && invalidFormatException.getTargetType().isEnum()) {
            
            String fieldName = getInvalidFieldName(invalidFormatException);

            message = fieldName + "：指定された値が不正です。使用可能な値： "
                    + String.join(", ", getEnumValues(invalidFormatException.getTargetType()));
        }

        ErrorResponse response = new ErrorResponse(
            status.value(), 
            status.getReasonPhrase(), 
            message, 
            request.getRequestURI()
        );

        return ResponseEntity.status(status).body(response);
    }

    private String getInvalidFieldName(InvalidFormatException ex) {
        if (ex.getPath().isEmpty()) {
            return "unknown";
        }

        return ex.getPath().get(ex.getPath().size() - 1).getFieldName();
    }

    private String[] getEnumValues(Class<?> enumType) {
        Object[] enumConstants = enumType.getEnumConstants();

        String[] values = new String[enumConstants.length];

        for (int i = 0; i < enumConstants.length; i++) {
            values[i] = enumConstants[i].toString();
        }

        return values;
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(
        ConflictException ex, HttpServletRequest request
    ) {

        HttpStatus status = HttpStatus.CONFLICT;

        ErrorResponse response = new ErrorResponse(
            status.value(), 
            status.getReasonPhrase(), 
            ex.getMessage(), 
            request.getRequestURI()
        );

        return ResponseEntity.status(status).body(response);
    }
}
