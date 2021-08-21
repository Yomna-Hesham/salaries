package com.yomna.salaries.util;

import com.yomna.salaries.model.Salary;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SalariesReportCsvUtil extends CsvUtil{
    public SalariesReportCsvUtil() {
        super();
        headers = Arrays.asList("name", "account_num", "salary", "currency", "failure_reason");
    }

    @Override
    protected List<?> mapRecords(List<CSVRecord> records) {
        return null;
    }

    @Override
    protected List<List<String>> mapData(List<?> data) {
        List<List<String>> mappedData = new ArrayList<>();

        for (Salary salary : (List<Salary>) data) {
            List<String> mappedSalary = new ArrayList<>();

            mappedSalary.add(salary.getName());
            mappedSalary.add(salary.getAccountNum());
            mappedSalary.add(String.valueOf(salary.getSalary()));
            mappedSalary.add(salary.getCurrency());
            mappedSalary.add(salary.getFailureReason());

            mappedData.add(mappedSalary);
        }

        return mappedData;
    }


}
