package com.project.foodapp.exception;

import com.project.foodapp.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = "com.project.foodapp.controller")
public class GlobalExceptionHandler {

    // ✅ Validation Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return new ApiResponse<>(
                false,
                "Validation failed",
                errors
        );
    }

    // ✅ Runtime Exceptions (like "User not found")
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<String> handleRuntimeException(RuntimeException ex) {
        return new ApiResponse<>(
                false,
                ex.getMessage(),
                null
        );
    }

    // ✅ Catch ALL other exceptions (VERY IMPORTANT for Swagger)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<String> handleAllExceptions(Exception ex) {
        return new ApiResponse<>(
                false,
                ex.getMessage(),
                null
        );
    }
}