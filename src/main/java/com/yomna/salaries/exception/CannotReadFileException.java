package com.yomna.salaries.exception;

public class CannotReadFileException extends Exception{
    public CannotReadFileException() {
        super(ErrorCodes.CANNOT_READ_FILE, "Something went wrong while reading the file. Please make sure the file is not corrupted or call the admin");
    }
}
