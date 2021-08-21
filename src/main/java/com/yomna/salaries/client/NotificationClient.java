package com.yomna.salaries.client;

import java.io.File;

public interface NotificationClient {
    void sendSms(String mobileNum, String msg);
    void sendEmail(String email, String body, File attachment);
}
