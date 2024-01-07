package com.notesSpringProj.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.notesSpringProj.payloads.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Exception handler for ResourceNotFoundException
    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){
        // Extract the error message from the exception
        String message = ex.getMessage();
        // Create an ApiResponse with the error message and set success to false
        ApiResponse apiResponse = new ApiResponse(message, false);
        // Return a ResponseEntity with the ApiResponse and HTTP status NOT_FOUND
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.NOT_FOUND);
    }

    // Exception handler for MethodArgumentNotValidException
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, String>> methodArgNotValidExHandler(MethodArgumentNotValidException ex) {
        // Create a map to store field errors and their corresponding error messages
        Map<String, String> resp = new HashMap<>();
        // Iterate through all validation errors
        ex.getBindingResult().getAllErrors().forEach((error)->{
            // Extract field name and error message
            String fieldName = ((FieldError)error).getField();
            String message = error.getDefaultMessage();
            // Put the field name and error message into the map
            resp.put(fieldName, message);
        });
        // Return a ResponseEntity with the map of field errors and HTTP status BAD_REQUEST
        return new ResponseEntity<Map<String,String>>(resp , HttpStatus.BAD_REQUEST);
    }

    // Exception handler for InvalidUserDetailsException
    @ExceptionHandler({InvalidUserDetailsException.class})
    public ResponseEntity<ApiResponse> InvalidUserDetailsExceptionHandler(InvalidUserDetailsException ex){
        // Extract the error message from the exception
        String message = ex.getMessage();
        // Create an ApiResponse with the error message and set success to false
        ApiResponse res = new ApiResponse(message, false);
        // Return a ResponseEntity with the ApiResponse and HTTP status NOT_ACCEPTABLE
        return new ResponseEntity<ApiResponse>(res, HttpStatus.NOT_ACCEPTABLE);
    }

    // Exception handler for TooManyRequestsException
    @ExceptionHandler({TooManyRequestsException.class})
    public ResponseEntity<ApiResponse> TooManyRequestsExceptionHandler(TooManyRequestsException ex){
        // Extract the error message from the exception
        String message = ex.getMessage();
        // Create an ApiResponse with the error message and set success to false
        ApiResponse res = new ApiResponse(message, false);
        // Return a ResponseEntity with the ApiResponse and HTTP status NOT_ACCEPTABLE
        return new ResponseEntity<ApiResponse>(res, HttpStatus.NOT_ACCEPTABLE);
    }

}
