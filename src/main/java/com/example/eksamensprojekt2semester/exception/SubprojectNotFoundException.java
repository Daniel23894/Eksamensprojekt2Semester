package com.example.eksamensprojekt2semester.exception;

public class SubprojectNotFoundException extends RuntimeException {
    public SubprojectNotFoundException(String message) {
        super(message);  /** Passes the message to the superclass (RuntimeException) **/
    }
}
