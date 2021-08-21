package com.yomna.salaries.client.demos;

import com.yomna.salaries.client.NotificationClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Component
public class SimpleNotificationClient implements NotificationClient {
    private static final Logger logger = LoggerFactory.getLogger(SimpleNotificationClient.class);

    @Value("${email.sender.address}") String senderEmail;
    @Autowired private JavaMailSender emailSender;

    @Override
    public void sendSms(String mobileNum, String msg) {
        logger.info("Sending SMS to {} - Body: {} ", mobileNum, msg);
    }

    @Override
    public void sendEmail(String email, String subject, String body, File attachment) {
        try {
            _sendEmail(email, subject, body, attachment);
        } catch (MessagingException e) {
            logger.error("sendEmail() | MessageException: {}", e.getMessage());
        }
    }

    private void _sendEmail(String email, String subject, String body, File attachment) throws MessagingException {
        logger.info("Sending Email to {} - Body: {} ", email, body);

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(senderEmail);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(body);
        if (attachment != null) {
            helper.addAttachment(attachment.getName(), attachment);
        }

        emailSender.send(message);
    }
}
