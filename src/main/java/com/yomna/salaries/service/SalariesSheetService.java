package com.yomna.salaries.service;

import com.yomna.salaries.client.NotificationClient;
import com.yomna.salaries.client.ResourcesClient;
import com.yomna.salaries.model.Salary;
import com.yomna.salaries.model.entity.Company;
import com.yomna.salaries.model.entity.SalariesSheet;
import com.yomna.salaries.model.entity.User;
import com.yomna.salaries.repository.SalariesSheetRepository;
import com.yomna.salaries.template.SalariesExecReportEmailTemplate;
import com.yomna.salaries.util.AuthorizationUtil;
import com.yomna.salaries.util.SalariesCsvUtil;
import com.yomna.salaries.util.SalariesReportCsvUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SalariesSheetService {
    private static final Logger logger = LoggerFactory.getLogger(SalariesSheetService.class);

    @Value("${salaries.sheets.root.path}") private String rootPath;

    @Autowired private SalariesSheetRepository repository;
    @Autowired private ResourcesClient resourcesClient;
    @Autowired private SalariesCsvUtil salariesCsvUtil;
    @Autowired private SalariesReportCsvUtil reportCsvUtil;
    @Autowired private AccountService accountService;
    @Autowired private NotificationClient notificationClient;

    public SalariesSheet saveSheet(MultipartFile file, Company company, String month) {
        String relativePath = generateSheetRelativePath(company);
        String filename = generateSheetFileName(file, month);
        resourcesClient.save(file, rootPath+relativePath, filename);

        return createSheet(company, month, relativePath, filename);
    }

    private String generateSheetRelativePath(Company company) {
        return  "/"+company.getId()+"#"+company.getName()+"/";
    }

    private String generateSheetFileName(MultipartFile file, String month) {
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
        sheet.setRelativePath(relativePath);
        sheet.setRootPath(rootPath);
        sheet.setSubmittedBy(AuthorizationUtil.getCurrentAuthorizedUser());

        return repository.save(sheet);
    }

    public void executeSheet(SalariesSheet sheet) {
        File salariesFile = resourcesClient.get(sheet.getRootPath() + sheet.getRelativePath(), sheet.getSheetFilename());
        List<Salary> salaries = (List<Salary>) salariesCsvUtil.parseFile(salariesFile);

        salaries = accountService.transferSalaries(salaries, sheet.getCompany());
        String reportCsv = reportCsvUtil.formatDataToCsv(salaries);

        String reportFilename = generateReportFilename(sheet);
        File reportFile = resourcesClient.save(reportCsv, rootPath + sheet.getRelativePath(), reportFilename);

        sheet.setReportFileName(reportFilename);
        sheet.setEvaluatedAt(LocalDateTime.now());
        repository.save(sheet);

        notifyUser(sheet.getSubmittedBy(), reportFile, salaries);
    }

    private String generateReportFilename(SalariesSheet sheet) {
        String sheetFilename = sheet.getSheetFilename();
        String extension = sheetFilename.substring(sheetFilename.lastIndexOf('.'));
        sheetFilename = sheetFilename.substring(0, sheetFilename.lastIndexOf('.'));

        return sheetFilename + "-report" + extension;
    }

    private void notifyUser(User user, File attachment, List<Salary> salaries) {
        long failureCount = salaries.stream().filter(salary -> salary.getFailureReason() != null).count();
        notificationClient.sendEmail(
                user.getEmail(),
                "Salaries Sheet Execution Report",
                new SalariesExecReportEmailTemplate((long) salaries.size(), failureCount).toString(),
                attachment);
    }
}
