package com.notesSpringProj.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidUserDetailsException extends RuntimeException{
    
    // Attributes to store details about the invalid user details exception
    private String resourceName;
    private String value;
    private Integer status;

    // Constructor to initialize the exception with specific details
    public InvalidUserDetailsException(String resourceName, String value, Integer status) {
        // Call the superclass constructor with a formatted error message
        super(String.format("%s Invalid with passed value %s, status : %s", resourceName, value, status));
        // Set attributes with provided values
        this.resourceName = resourceName;
        this.value = value;
        this.status = status;
    }
}
