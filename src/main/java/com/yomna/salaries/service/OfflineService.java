package com.yomna.salaries.service;

import com.yomna.salaries.model.SalariesSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Lazy
@Async
@Service
@Transactional
public class OfflineService {
    private static final Logger logger = LoggerFactory.getLogger(OfflineService.class);

    @Autowired private SalariesSheetService salariesSheetService;

    public void executeSalariesSheet(SalariesSheet sheet) {
        salariesSheetService.executeSheet(sheet);
    }
}


