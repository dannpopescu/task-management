package com.danpopescu.taskmanagement.exceptions;

public class TaskNotFoundException extends Exception {

    public TaskNotFoundException() {
        super();
    }

    public TaskNotFoundException(String message) {
        super(message);
    }
}
