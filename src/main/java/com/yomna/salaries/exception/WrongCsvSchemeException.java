package com.yomna.salaries.exception;

public class WrongCsvSchemeException extends Exception{
    public WrongCsvSchemeException() {
        super(ErrorCodes.WRONG_CSV_SCHEME, "Wrong CSV scheme");
    }
}
