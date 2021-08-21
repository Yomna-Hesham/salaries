package com.yomna.salaries.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExceptionResponse {
    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;
}
