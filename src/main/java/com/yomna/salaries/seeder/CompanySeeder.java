package com.yomna.salaries.seeder;

import com.yomna.salaries.model.Company;
import com.yomna.salaries.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class CompanySeeder {
    private static final Logger logger = LoggerFactory.getLogger(CompanySeeder.class);

    @Autowired private CompanyService service;

    @PostConstruct
    public void seed() {
        logger.info("seed() | Start ...");

        List<Company> companies = new ArrayList<>();
        companies.add(new Company("ABC"));
        companies.add(new Company("XYZ"));
        companies.add(new Company("eVision"));

        companies = service.saveCompanies(companies);
        logger.debug("seed() | Seeded Companies: {}", companies);

        logger.info("seed() | ... Finish");
    }
}
