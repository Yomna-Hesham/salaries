package com.yomna.salaries.exception;

public class UnsupportedFileTypeException extends Exception{
    public UnsupportedFileTypeException() {
        super(ErrorCodes.UNSUPPORTED_FILE_TYPE, "only csv, xls, xlsx files are supported");
    }
}
