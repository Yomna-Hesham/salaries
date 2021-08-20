package com.yomna.salaries.controller;

import com.yomna.salaries.service.CompanyService;
import com.yomna.salaries.util.SalariesCsvUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/company")
public class CompanyController {
    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);

    @Autowired private SalariesCsvUtil salariesCsvUtil;
    @Autowired private CompanyService service;

    @PostMapping(value = "/{company_id}/salary")
    public void addSalaries(
            @PathVariable("company_id") Integer companyId,
            @RequestPart("file") MultipartFile file,
            @RequestPart("month") String month,
            @RequestPart("year") String year
    ) {
        logger.info("File: {}", file);
        salariesCsvUtil.validateFileFormat(file);

        service.receiveSalariesSheet(companyId, (month + year).toLowerCase(), file);
    }
}
