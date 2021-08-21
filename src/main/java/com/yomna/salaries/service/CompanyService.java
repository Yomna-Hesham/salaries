package com.yomna.salaries.service;

import com.yomna.salaries.exception.NotFoundException;
import com.yomna.salaries.model.entity.Company;
import com.yomna.salaries.model.entity.SalariesSheet;
import com.yomna.salaries.repository.CompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CompanyService {
    private static final Logger logger = LoggerFactory.getLogger(CompanyService.class);

    @Autowired private CompanyRepository repository;
    @Autowired private SalariesSheetService salariesSheetService;
    @Autowired private OfflineService offlineService;

    public void receiveSalariesSheet(Integer companyId, String month, MultipartFile file) {
        logger.info("receiveSalariesSheet() | Start ... Company Id: {}, Month: {}", companyId, month);

        Company company = getCompany(companyId);
        SalariesSheet sheet = salariesSheetService.saveSheet(file, company, month);
        offlineService.executeSalariesSheet(sheet);
    }

    public Company getCompany(Integer companyId) {
        logger.info("getCompany() | Start ... companyId: {}", companyId);

        Company company = repository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("No company with id " + companyId));
        logger.debug("getCompany() | company: {}", company);

        return company;
    }

}
