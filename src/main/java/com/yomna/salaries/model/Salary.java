package com.yomna.salaries.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Salary {
    private String name;
    private String accountNum;
    private Double salary;
    private String currency;
    private String failureReason;
}
