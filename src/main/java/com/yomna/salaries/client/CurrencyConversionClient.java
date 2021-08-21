package com.yomna.salaries.client;

public interface CurrencyConversionClient {
    Double convert(String from, String to, Double amount);
    Double getConversionFees(String from, String to, Double amount);
}
