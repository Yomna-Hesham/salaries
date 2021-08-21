package com.yomna.salaries.client.demos;

import com.yomna.salaries.client.CurrencyConversionClient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SimpleCurrencyConverter implements CurrencyConversionClient {
    private final Map<String, Double> conversionRates;
    private final Map<String, Double> conversionFeesRates;
    private final Map<String, Double> conversionFeesThresholds;

    public SimpleCurrencyConverter() {
        conversionRates = new HashMap<>();
        conversionRates.put("EGP-USD", 0.064);
        conversionRates.put("EGP-SAR", 0.24);
        conversionRates.put("USD-EGP", 15.70);
        conversionRates.put("SAR-EGP", 4.19);

        conversionFeesRates = new HashMap<>();
        conversionFeesRates.put("EGP-USD", 0.01);
        conversionFeesRates.put("EGP-SAR", 0.04);
        conversionFeesRates.put("USD-EGP", 0.01);
        conversionFeesRates.put("SAR-EGP", 0.04);

        conversionFeesThresholds = new HashMap<>();
        conversionFeesThresholds.put("EGP-USD", 500.0);
        conversionFeesThresholds.put("EGP-SAR", 250.0);
        conversionFeesThresholds.put("USD-EGP", 100.0);
        conversionFeesThresholds.put("SAR-EGP", 100.0);
    }

    @Override
    public Double convert(String from, String to, Double amount) {
        Double conversionRate = conversionRates.get(from+"-"+to);
        if (conversionRate == null) {
            conversionRate = 1.0;
        }

        return amount * conversionRate;
    }

    @Override
    public Double getConversionFees(String from, String to, Double amount) {
        Double conversionFeesRate = conversionFeesRates.get(from+"-"+to);
        if (conversionFeesRate == null) {
            return 0.0;
        }

        Double conversionFeesThreshold = conversionFeesThresholds.get(from+"-"+to);
        Double conversionFees = conversionFeesRate * amount;

        return conversionFees < conversionFeesThreshold ? conversionFees : conversionFeesThreshold;
    }
}
