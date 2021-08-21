package com.yomna.salaries.service;

import com.yomna.salaries.client.ResourcesClient;
import com.yomna.salaries.model.entity.Company;
import com.yomna.salaries.model.entity.SalariesSheet;
import com.yomna.salaries.model.Salary;
import com.yomna.salaries.repository.SalariesSheetRepository;
import com.yomna.salaries.util.AuthorizationUtil;
import com.yomna.salaries.util.SalariesCsvUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.Instant;
import java.util.List;

@Service
public class SalariesSheetService {
    private static final Logger logger = LoggerFactory.getLogger(SalariesSheetService.class);

    @Value("${salaries.sheets.root.path}") private String rootPath;

    @Autowired private SalariesSheetRepository repository;
    @Autowired private ResourcesClient resourcesClient;
    @Autowired private SalariesCsvUtil salariesCsvUtil;
    @Autowired private AccountService accountService;

    public SalariesSheet saveSheet(MultipartFile file, Company company, String month) {
        String relativePath = generateSheetRelativePath(company);
        String filename = generateFileName(file, month);
        resourcesClient.save(file, rootPath+relativePath, filename);

        return createSheet(company, month, relativePath, filename);
    }

    private String generateSheetRelativePath(Company company) {
        return  "/"+company.getId()+"#"+company.getName()+"/";
    }

    private String generateFileName(MultipartFile file, String month) {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        long timestamp = Instant.now().getEpochSecond();

        return month+"-"+timestamp+extension;
    }

    public SalariesSheet createSheet(Company company, String month, String relativePath, String filename) {
        SalariesSheet sheet = new SalariesSheet();

        sheet.setCompany(company);
        sheet.setMonth(month);
        sheet.setSheetFilename(filename);
        sheet.setSheetRelativePath(relativePath);
        sheet.setRootPath(rootPath);
        sheet.setSubmittedBy(AuthorizationUtil.getCurrentAuthorizedUser());

        return repository.save(sheet);
    }

    public void executeSheet(SalariesSheet sheet) {
        File file = resourcesClient.get(sheet.getRootPath() + sheet.getSheetRelativePath(), sheet.getSheetFilename());
        List<Salary> salaries = (List<Salary>) salariesCsvUtil.parseFile(file);
        logger.debug("Salaries: {}", salaries);

        salaries = accountService.transferSalaries(salaries, sheet.getCompany());
    }
}
