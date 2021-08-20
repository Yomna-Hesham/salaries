package com.yomna.salaries.service;

import com.yomna.salaries.exception.NotFoundException;
import com.yomna.salaries.model.Company;
import com.yomna.salaries.repository.CompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class CompanyService {
    private static final Logger logger = LoggerFactory.getLogger(CompanyService.class);

    @Autowired private CompanyRepository repository;
    @Autowired private SalariesSheetService salariesSheetService;

    public void receiveSalariesSheet(Integer companyId, String month, MultipartFile file) {
        logger.info("receiveSalariesSheet() | Start ... Company Id: {}, Month: {}", companyId, month);

        Company company = getCompany(companyId);
        salariesSheetService.saveSheet(file, company, month);

    }

    public Company getCompany(Integer companyId) {
        logger.info("getCompany() | Start ... companyId: {}", companyId);

        Company company = repository.findById(companyId).orElseThrow(() -> new NotFoundException("No company with id " + companyId));
        logger.debug("getCompany() | company: {}", company);

        return company;
    }

    public List<Company> saveCompanies(List<Company> companies) {
        return repository.saveAll(companies);
    }
}
