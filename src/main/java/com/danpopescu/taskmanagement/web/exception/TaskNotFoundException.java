package com.danpopescu.taskmanagement.web.exception;

public class TaskNotFoundException extends Exception {

    public TaskNotFoundException() {
        super();
    }

    public TaskNotFoundException(String message) {
        super(message);
    }
}
