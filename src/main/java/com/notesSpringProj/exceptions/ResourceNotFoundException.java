package com.notesSpringProj.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResourceNotFoundException extends RuntimeException {

    // Attributes to store details about the resource not found exception
    String resourceName;
    String fieldName;
    long fieldValue;

    // Constructor with parameters to initialize the exception with specific details
    public ResourceNotFoundException(String resourceName, String fieldName, long fieldValue) {
        // Call the superclass constructor with a formatted error message
        super(String.format("%s not found with %s : %s", resourceName, fieldName, fieldValue));
        // Set attributes with provided values
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
