package com.yomna.salaries.exception;

public class NotFoundException extends Exception{

    public NotFoundException(String message) {
        super(ErrorCodes.NOT_FOUND, message);
    }
}
