package com.yomna.salaries.util;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SalariesCsvUtil extends CsvUtil{
    public SalariesCsvUtil() {
        super();
        headers = Arrays.asList("name", "account_num", "salary", "currency");
    }
}
