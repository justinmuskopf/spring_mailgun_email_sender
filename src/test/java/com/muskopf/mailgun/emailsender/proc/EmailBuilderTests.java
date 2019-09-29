package com.muskopf.mailgun.emailsender.proc;

import com.muskopf.mailgun.emailsender.config.EmailSenderConfiguration;
import com.muskopf.mailgun.emailsender.domain.Email;
import com.muskopf.mailgun.emailsender.domain.NoBodyException;
import com.muskopf.mailgun.emailsender.domain.NoRecipientsException;
import com.muskopf.mailgun.emailsender.domain.NoSubjectException;
import com.muskopf.mailgun.emailsender.proc.EmailBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        EmailBuilder.class,
})
@EnableConfigurationProperties(EmailSenderConfiguration.class)
public class EmailBuilderTests {
    @Autowired
    private EmailBuilder emailBuilder;

    @Autowired
    private EmailSenderConfiguration configuration;

    private String SUBJECT = "Subject";
    private String BODY = "Email body";
    private String DUMMY_ADDRESS = "test@testing.com";
    private String SENDER_EMAIL = "info@sender.com";
    private String SENDER_NAME = "Testy Testerson";
    private String SENDER_FIELD = "Testy Testerson <info@sender.com>";

    private Set<String> getEmailSet() {
        Set<String> emailSet = new HashSet<>();
        emailSet.add(DUMMY_ADDRESS);

        return emailSet;
    }

    private String buildSenderField(String senderName, String senderEmail) {
        return senderName + " " + "<" + senderEmail + ">";
    }

    @Test
    public void testValidEmail() {
        Email testAgainst = new Email();
        testAgainst.setSubject(SUBJECT);
        testAgainst.setBody(BODY);
        testAgainst.addRecipient(DUMMY_ADDRESS);
        testAgainst.setSender(SENDER_FIELD);

        Email email = emailBuilder
                .builder()
                .subject(SUBJECT)
                .body(BODY)
                .addRecipient(DUMMY_ADDRESS)
                .sender(SENDER_NAME, SENDER_EMAIL)
                .build();

        assertThat(email).isEqualToComparingFieldByFieldRecursively(testAgainst);
    }

    @Test
    public void testDefaultValuesFromPropertiesFile() {
        Email email = emailBuilder
                .builder()
                .useDefaults()
                .body("Hello!")
                .build();

        assertThat(email.getBody()).isNotEmpty();
        assertThat(email.getSubject()).isEqualTo(configuration.getDefaultSubject());
        assertThat(email.getRecipients()).isEqualTo(configuration.getDefaultRecipients());
        assertThat(email.getSender()).isEqualTo(buildSenderField(configuration.getDefaultSenderName(),
                configuration.getDefaultSenderEmailAddress()));
    }

    @Test
    public void testDefaultValuesSetProgramatically() {
        Set<String> defaultRecipients = getEmailSet();
        emailBuilder.setDefaultRecipients(defaultRecipients);
        emailBuilder.setDefaultSubject(SUBJECT);
        emailBuilder.setDefaultSenderEmailAddress(SENDER_EMAIL);
        emailBuilder.setDefaultSenderName(SENDER_NAME);

        Email email = emailBuilder
                .builder()
                .useDefaults()
                .body(BODY)
                .build();

        assertThat(email.getRecipients()).isEqualTo(defaultRecipients);
        assertThat(email.getSubject()).isEqualTo(SUBJECT);
        assertThat(email.getSender()).isEqualTo(SENDER_FIELD);
    }

    @Test
    public void testDefaultValuesUsingUseDefaultMethods() {
        Email email = emailBuilder
                .builder()
                .useDefaultRecipients()
                .useDefaultSender()
                .useDefaultSubject()
                .body(BODY)
                .build();

        assertThat(email.getRecipients()).isEqualTo(configuration.getDefaultRecipients());
        assertThat(email.getSender()).isEqualTo(buildSenderField(configuration.getDefaultSenderName(),
                configuration.getDefaultSenderEmailAddress()));
        assertThat(email.getSubject()).isEqualTo(configuration.getDefaultSubject());
    }

    @Test(expected = NoSubjectException.class)
    public void testNoSubjectThrowsNoSubjectException() {
        Email email = emailBuilder
                .builder()
                .body(BODY)
                .addRecipient(DUMMY_ADDRESS)
                .sender(SENDER_NAME, SENDER_EMAIL)
                .build();
    }

    @Test(expected = NoBodyException.class)
    public void testNoBodyThrowsNoBodyException() {
        Email email = emailBuilder
                .builder()
                .subject(SUBJECT)
                .addRecipient(DUMMY_ADDRESS)
                .sender(SENDER_NAME, SENDER_EMAIL)
                .build();
    }

    @Test(expected = NoRecipientsException.class)
    public void testNoRecipientsThrowsNoRecipientsException() {
        Email email = emailBuilder
                .builder()
                .subject(SUBJECT)
                .body(BODY)
                .sender(SENDER_NAME, SENDER_EMAIL)
                .build();
    }
}
