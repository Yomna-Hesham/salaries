package com.yomna.salaries.template;

import java.time.LocalDateTime;

public class SalaryTransferSmsTemplate extends SmsTemplate{

    public SalaryTransferSmsTemplate() {
        super();
        body = "your salary has been transferred to your account.";
        timestamp = LocalDateTime.now();
    }
}
