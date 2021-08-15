package com.yomna.salaries.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class Exception extends RuntimeException{
    private String code;
    private String message;
}
