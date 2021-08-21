package com.yomna.salaries.util;

import com.yomna.salaries.model.Salary;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SalariesCsvUtil extends CsvUtil{
    public SalariesCsvUtil() {
        super();
        headers = Arrays.asList("name", "account_num", "salary", "currency");
    }

    @Override
    protected List<Salary> mapRecords(List<CSVRecord> records) {
        List<Salary> salaries = new ArrayList<>();
        for (CSVRecord record : records) {
            Salary salary = new Salary();

            salary.setName(record.get("name"));
            salary.setAccountNum(record.get("account_num"));
            salary.setSalary(
                    !record.get("salary").equals("") ?
                            Double.parseDouble(record.get("salary")) : null);
            salary.setCurrency(record.get("currency"));

            salaries.add(salary);
        }

        return salaries;
    }

    @Override
    protected List<List<String>> mapData(List<?> data) {
        return null;
    }
}
