package com.yomna.salaries.exception;

public class SalaryTransferFailureException extends Exception{
    public SalaryTransferFailureException(String message) {
        super(ErrorCodes.SALARY_TRANSFER_FAILURE, message);
    }
}
