package com.muskopf.mailgun.emailsender;

import com.muskopf.mailgun.emailsender.config.EmailSenderConfiguration;
import com.muskopf.mailgun.emailsender.domain.Email;
import com.muskopf.mailgun.emailsender.domain.MailgunResponse;
import com.muskopf.mailgun.emailsender.impl.SpringMailgunEmailSenderImpl;
import com.muskopf.mailgun.emailsender.proc.EmailBuilder;
import com.muskopf.mailgun.emailsender.proc.EmailValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        SpringMailgunEmailSenderImpl.class,
        EmailValidator.class,
        EmailBuilder.class
})
@EnableConfigurationProperties(EmailSenderConfiguration.class)
public class EmailSenderTests {
    @Autowired
    EmailSender emailSender;

    @Autowired
    EmailBuilder emailBuilder;

    @Test
    public void testSendEmail() {
        Email email = emailBuilder.builder()
                .body(UUID.randomUUID().toString())
                .subject(UUID.randomUUID().toString())
                .useDefaultRecipients()
                .useDefaultSender()
                .build();

        MailgunResponse response = emailSender.sendEmail(email);
    }
}
