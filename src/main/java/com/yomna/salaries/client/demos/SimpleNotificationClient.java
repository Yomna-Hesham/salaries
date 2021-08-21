package com.yomna.salaries.client.demos;

import com.yomna.salaries.client.NotificationClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class SimpleNotificationClient implements NotificationClient {
    private static final Logger logger = LoggerFactory.getLogger(SimpleNotificationClient.class);

    @Override
    public void sendSms(String mobileNum, String msg) {
        logger.info("Sending SMS to {} - Body: {} ", mobileNum, msg);
    }

    @Override
    public void sendEmail(String email, String body, File attachment) {
        logger.info("Sending Email to {} - Body: {} ", email, body);
    }
}
