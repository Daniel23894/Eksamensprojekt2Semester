package com.example.eksamensprojekt2semester.exception;

public class ProjectNotFoundException extends RuntimeException {

    public ProjectNotFoundException(String message) {
        super(message);  /** Passes the message to the superclass (RuntimeException) **/
    }

}
