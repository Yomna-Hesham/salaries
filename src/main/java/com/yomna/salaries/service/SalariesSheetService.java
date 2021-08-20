package com.yomna.salaries.service;

import com.yomna.salaries.client.ResourcesClient;
import com.yomna.salaries.model.Company;
import com.yomna.salaries.model.SalariesSheet;
import com.yomna.salaries.repository.SalariesSheetRepository;
import com.yomna.salaries.util.AuthorizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Service
public class SalariesSheetService {
    @Value("${salaries.sheets.root.path}") private String rootPath;
    @Autowired private SalariesSheetRepository repository;
    @Autowired private ResourcesClient resourcesClient;

    public SalariesSheet saveSheet(MultipartFile file, Company company, String month) {
        String relativePath = generateSheetRelativePath(company);
        String fileName = generateFileName(file, month);
        resourcesClient.save(file, rootPath+relativePath, fileName);

        return createSheet(company, month, relativePath, fileName);
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

    public SalariesSheet createSheet(Company company, String month, String relativePath, String fileName) {
        SalariesSheet sheet = new SalariesSheet();

        sheet.setCompany(company);
        sheet.setMonth(month);
        sheet.setSheetFileName(fileName);
        sheet.setSheetRelativePath(relativePath);
        sheet.setRootPath(rootPath);
        sheet.setSubmittedBy(AuthorizationUtil.getCurrentAuthorizedUser());

        return repository.save(sheet);
    }

    public void executeSheet(SalariesSheet sheet) {

    }
}
